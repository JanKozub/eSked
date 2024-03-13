package org.jk.eSked.ui.components.login;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import org.jk.eSked.backend.model.User;
import org.jk.eSked.backend.model.types.EmailType;
import org.jk.eSked.backend.model.types.NotificationType;
import org.jk.eSked.backend.service.EmailService;
import org.jk.eSked.backend.service.TimeService;
import org.jk.eSked.backend.service.user.UserService;
import org.jk.eSked.ui.components.myComponents.SuccessNotification;

import javax.validation.ValidationException;
import java.util.Collection;
import java.util.UUID;

class NewUserDialog extends Dialog {

    NewUserDialog(UserService userService, EmailService emailService) {
        VerticalLayout layout = new VerticalLayout();

        Label nameOfDialog = new Label(getTranslation("new.user.label"));

        TextField usernameField = new TextField(getTranslation("username"));
        usernameField.setWidth("100%");

        EmailField emailField = new EmailField("Email");
        emailField.setWidth("100%");

        PasswordField passwordField = new PasswordField(getTranslation("password"));
        passwordField.setWidth("100%");

        PasswordField passwordFieldCheck = new PasswordField(getTranslation("new.user.repeat.password"));
        passwordFieldCheck.setWidth("100%");

        Button addButton = new Button(getTranslation("new.user.register"));
        addButton.addClickShortcut(Key.ENTER);
        addButton.setWidth("75%");
        addButton.addClickListener(e -> {//TODO simplify try catch
            try {
                validateUsername(usernameField.getValue(), userService);
                usernameField.setInvalid(false);
                try {
                    validateEmail(emailField.getValue(), userService);
                    emailField.setInvalid(false);
                    try {
                        validatePassword(passwordField.getValue(), passwordFieldCheck.getValue());
                        passwordField.setInvalid(false);
                        passwordFieldCheck.setInvalid(false);

                        User user = new User(UUID.randomUUID(),
                                usernameField.getValue(),
                                User.encodePassword(passwordField.getValue()),
                                false,
                                true,
                                emailField.getValue(),
                                0,
                                false,
                                false,
                                TimeService.now(),
                                TimeService.now(),
                                true);

                        emailService.sendEmail(user, EmailType.NEWUSER);
                        userService.addUser(user);

                        new SuccessNotification(getTranslation("notification.link.sent"), NotificationType.LONG).open();
                        close();
                    } catch (ValidationException ex) {
                        passwordFieldCheck.setErrorMessage(ex.getMessage());
                        passwordField.setInvalid(true);
                        passwordFieldCheck.setInvalid(true);
                    } catch (Exception mex) {
                        passwordFieldCheck.setErrorMessage(getTranslation("exception.contact.admin") + " " + mex.getMessage());
                        passwordFieldCheck.setInvalid(true);
                    }
                } catch (ValidationException ex) {
                    emailField.setErrorMessage(ex.getMessage());
                    emailField.setInvalid(true);
                }
            } catch (ValidationException ex) {
                usernameField.setErrorMessage(ex.getMessage());
                usernameField.setInvalid(true);
            }
        });
        layout.add(nameOfDialog, usernameField, emailField, passwordField, passwordFieldCheck, addButton);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        setWidth("300px");
        add(layout);
    }

    private void validateUsername(String username, UserService userService) {
        if (username.isEmpty()) throw new ValidationException(getTranslation("exception.empty.field"));

        Collection<String> usernames = userService.getUsernames();
        if (usernames.contains(username))
            throw new ValidationException(getTranslation("exception.user.exists"));
    }

    private void validateEmail(String email, UserService userService) {
        if (email.isEmpty()) throw new ValidationException(getTranslation("exception.empty.field"));

        Collection<String> emails = userService.getEmails();
        if (emails.contains(email)) throw new ValidationException(getTranslation("exception.email.taken"));
    }

    private void validatePassword(String pass1, String pass2) {
        if (pass1.isEmpty()) throw new ValidationException(getTranslation("exception.fields.cannot.be.empty"));

        if (!pass1.equals(pass2)) throw new ValidationException(getTranslation("exception.password.not.match"));
    }
}
