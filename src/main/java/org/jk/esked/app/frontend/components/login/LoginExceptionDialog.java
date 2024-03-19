package org.jk.esked.app.frontend.components.login;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.jk.esked.app.backend.model.exceptions.ValidationException;
import org.jk.esked.app.backend.model.types.EmailType;
import org.jk.esked.app.backend.model.types.NotificationType;
import org.jk.esked.app.backend.services.EmailService;
import org.jk.esked.app.backend.services.EncryptionService;
import org.jk.esked.app.backend.services.UserService;
import org.jk.esked.app.frontend.components.SuccessNotification;

import java.util.List;

public class LoginExceptionDialog extends Dialog {
    private final EmailService emailService;

    public LoginExceptionDialog(UserService userService, EmailService emailService, EncryptionService encryptionService) {
        this.emailService = emailService;

        Text newUserLabel = new Text(getTranslation("login.exception.label"));
//        newUserLabel.getStyle().set("font-weight", "bold");

        Button newUser = new Button(getTranslation("login.exception.button"), click -> {
            Dialog dialog = new NewUserDialog(userService, emailService, encryptionService);
            dialog.open();
        });
        newUser.setWidth("100%");

        Text passLabel = new Text(getTranslation("login.exception.password"));
//        passLabel.getStyle().set("font-weight", "bold");

        TextField usernameField = new TextField();
        usernameField.setPlaceholder(getTranslation("username"));
        usernameField.setWidth("60%");

        Button confirmButton = new Button(getTranslation("login.exception.email"));
        confirmButton.addClickShortcut(Key.ENTER);
        confirmButton.setWidth("40%");
        confirmButton.addClickListener(event -> onConfirm(userService, usernameField));

        HorizontalLayout passFieldLayout = new HorizontalLayout(usernameField, confirmButton);
        passFieldLayout.setWidth("100%");

        VerticalLayout mainLayout = new VerticalLayout(newUserLabel, newUser, passLabel, passFieldLayout);
        mainLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        add(mainLayout);
    }

    private void onConfirm(UserService userService, TextField usernameField) {
        try {
            validateUsernameInput(userService, usernameField.getValue());
            usernameField.setInvalid(false);

            new SuccessNotification(getTranslation("notification.reset.link.sent"), NotificationType.LONG).open();
            emailService.sendEmail(userService.getUserById(userService.getIdByUsername(usernameField.getValue())), EmailType.NEWPASSOWRD);
        } catch (Exception ex) {
            usernameField.setErrorMessage(ex.getMessage());
            usernameField.setInvalid(true);
        }
    }

    private void validateUsernameInput(UserService userService, String input) throws ValidationException {
        if (input.isEmpty()) throw new ValidationException(getTranslation("exception.empty.field"));

        List<String> users = userService.getAllUserUsernames();
        if (!users.contains(input)) throw new ValidationException(getTranslation("exception.user.not.exist"));
    }
}
