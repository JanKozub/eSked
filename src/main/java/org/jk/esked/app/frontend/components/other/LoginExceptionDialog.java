package org.jk.esked.app.frontend.components.other;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import org.jk.esked.app.backend.model.entities.User;
import org.jk.esked.app.backend.model.exceptions.EmailValidationException;
import org.jk.esked.app.backend.model.exceptions.FieldValidationException;
import org.jk.esked.app.backend.model.exceptions.ValidationException;
import org.jk.esked.app.backend.model.types.EmailType;
import org.jk.esked.app.backend.model.types.FieldType;
import org.jk.esked.app.backend.model.types.NotificationType;
import org.jk.esked.app.backend.security.SecurityService;
import org.jk.esked.app.backend.services.UserService;
import org.jk.esked.app.backend.services.utilities.EmailService;
import org.jk.esked.app.frontend.components.notifications.SuccessNotification;

import java.util.List;

public class LoginExceptionDialog extends Dialog {
    private final UserService userService;
    private final EmailService emailService;
    private final SecurityService securityService;

    public LoginExceptionDialog(UserService userService, EmailService emailService, SecurityService securityService) {
        this.userService = userService;
        this.emailService = emailService;
        this.securityService = securityService;

        Button closeButton = new Button(new Icon("lumo", "cross"), e -> close());
        closeButton.addClassName("close-button");
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        getHeader().add(closeButton);

        setWidth("800px");
        add(new HorizontalLayout(layoutLeft(), new VerticalLine(), layoutRight()));
    }

    private VerticalLayout layoutLeft() {
        VerticalLayout layout = new VerticalLayout();

        Text nameOfDialog = new Text(getTranslation("new.user.label"));
        TextField usernameField = new TextField(getTranslation("username"));
        EmailField emailField = new EmailField("Email");
        PasswordField passwordField = new PasswordField(getTranslation("password"));
        PasswordField passwordFieldCheck = new PasswordField(getTranslation("new.user.repeat.password"));

        Button addButton = new Button(getTranslation("new.user.register"),
                e -> addUser(usernameField, emailField, passwordField, passwordFieldCheck));
        addButton.addClickShortcut(Key.ENTER);
        layout.add(nameOfDialog, usernameField, emailField, passwordField, passwordFieldCheck, addButton);
        layout.addClassName("login-layout");
        return layout;
    }

    private VerticalLayout layoutRight() {
        Span passLabel = new Span(getTranslation("login.exception.password"));
        passLabel.addClassName("pass-label");
        TextField usernameField = new TextField(getTranslation("username"));
        Span orLabel = new Span(getTranslation("or"));
        orLabel.addClassName("or-label");
        EmailField emailField = new EmailField("Email");

        Button confirmButton = new Button(getTranslation("login.exception.email"),
                e -> onConfirm(usernameField, emailField));

        VerticalLayout layoutRight = new VerticalLayout(passLabel, usernameField, orLabel, emailField, confirmButton);
        layoutRight.addClassName("login-layout");
        return layoutRight;
    }

    private void addUser(TextField usernameField, EmailField emailField, PasswordField passwordField, PasswordField passwordFieldCheck) {
        try {
            validateUsername(usernameField.getValue());
            usernameField.setInvalid(false);

            validateEmail(emailField.getValue());
            emailField.setInvalid(false);

            validatePassword(passwordField.getValue(), passwordFieldCheck.getValue());
            passwordField.setInvalid(false);
            passwordFieldCheck.setInvalid(false);

            User user = new User(usernameField.getValue(),
                    securityService.encodePassword(passwordField.getValue()),
                    emailField.getValue());
            user.setVerified(true);

            emailService.sendEmail(user, EmailType.NEWUSER);
            userService.saveUser(user);

            new SuccessNotification(getTranslation("notification.link.sent"), NotificationType.LONG).open();
            close();
        } catch (FieldValidationException ex) {
            switch (ex.getFieldType()) {
                case USERNAME -> {
                    usernameField.setErrorMessage(ex.getMessage());
                    usernameField.setInvalid(true);
                }
                case EMAIL -> {
                    emailField.setErrorMessage(ex.getMessage());
                    emailField.setInvalid(true);
                }
                case PASSWORD -> {
                    passwordFieldCheck.setErrorMessage(ex.getMessage());
                    passwordField.setInvalid(true);
                    passwordFieldCheck.setInvalid(true);
                }
            }
        } catch (Exception mex) {
            passwordFieldCheck.setErrorMessage(getTranslation("exception.contact.admin") + " " + mex.getMessage());
            passwordFieldCheck.setInvalid(true);
        }
    }

    private void validateUsername(String username) throws FieldValidationException {
        if (username.isEmpty())
            throw new FieldValidationException(getTranslation("exception.empty.field"), FieldType.USERNAME);

        List<String> usernames = userService.findAllUsernames();
        if (usernames.contains(username))
            throw new FieldValidationException(getTranslation("exception.user.exists"), FieldType.USERNAME);
    }

    private void validateEmail(String email) throws FieldValidationException {
        if (email.isEmpty())
            throw new FieldValidationException(getTranslation("exception.empty.field"), FieldType.EMAIL);

        List<String> emails = userService.findAllRegisteredEmails();
        if (emails.contains(email))
            throw new FieldValidationException(getTranslation("exception.email.taken"), FieldType.EMAIL);
    }

    private void validatePassword(String pass1, String pass2) throws FieldValidationException {
        if (pass1.isEmpty())
            throw new FieldValidationException(getTranslation("exception.fields.cannot.be.empty"), FieldType.PASSWORD);

        if (!pass1.equals(pass2))
            throw new FieldValidationException(getTranslation("exception.password.not.match"), FieldType.PASSWORD);
    }

    private void onConfirm(TextField usernameField, EmailField emailField) {
        try {
            User user;
            if (usernameField.isEmpty()) {
                validateEmailInput(emailField.getValue());
                emailField.setInvalid(false);
                user = userService.findUserByEmail(emailField.getValue());
            } else {
                validateUsernameInput(usernameField.getValue(), emailField.getValue());
                usernameField.setInvalid(false);
                user = userService.findUserByUsername(usernameField.getValue());
            }

            new SuccessNotification(getTranslation("notification.reset.link.sent"), NotificationType.LONG).open();
            emailService.sendEmail(user, EmailType.NEWPASSOWRD);
        } catch (EmailValidationException ex) {
            emailField.setErrorMessage(ex.getMessage());
            emailField.setInvalid(true);
        } catch (Exception ex) {
            usernameField.setErrorMessage(ex.getMessage());
            usernameField.setInvalid(true);
        }
    }

    private void validateUsernameInput(String input, String input2) throws ValidationException {
        if (input.isEmpty()) throw new ValidationException(getTranslation("exception.empty.field"));

        if (!input2.isEmpty()) throw new ValidationException(getTranslation("exception.fields.filled"));

        List<String> users = userService.findAllUsernames();
        if (!users.contains(input)) throw new ValidationException(getTranslation("exception.user.not.exist"));
    }

    private void validateEmailInput(String value) throws ValidationException {
        if (value.isEmpty()) throw new EmailValidationException(getTranslation("exception.empty.field"));
    }
}
