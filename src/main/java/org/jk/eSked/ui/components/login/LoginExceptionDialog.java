package org.jk.eSked.ui.components.login;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.jk.eSked.backend.model.exceptions.ValidationException;
import org.jk.eSked.backend.model.types.EmailType;
import org.jk.eSked.backend.model.types.NotificationType;
import org.jk.eSked.backend.service.EmailService;
import org.jk.eSked.backend.service.user.UserService;
import org.jk.eSked.ui.components.myComponents.SuccessNotification;

import java.util.Collection;

public class LoginExceptionDialog extends Dialog {
    public LoginExceptionDialog(UserService userService, EmailService emailService) {

        Text newUserLabel = new Text(getTranslation("login.exception.label"));
        newUserLabel.getStyle().set("font-weight", "bold");

        Button newUser = new Button(getTranslation("login.exception.button"), click -> {
            close();
            new NewUserDialog(userService, emailService).open();
        });
        newUser.setWidth("100%");

        Text passLabel = new Text(getTranslation("login.exception.password"));
        passLabel.getStyle().set("font-weight", "bold");

        TextField usernameField = new TextField();
        usernameField.setPlaceholder(getTranslation("username"));
        usernameField.setWidth("60%");

        Button confirmButton = new Button(getTranslation("login.exception.email"));
        confirmButton.addClickShortcut(Key.ENTER);
        confirmButton.setWidth("40%");
        confirmButton.addClickListener(event -> onConfirm(userService, emailService, usernameField));

        HorizontalLayout passFieldLayout = new HorizontalLayout(usernameField, confirmButton);
        passFieldLayout.setWidth("100%");

        VerticalLayout mainLayout = new VerticalLayout(newUserLabel, newUser, passLabel, passFieldLayout);
        mainLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        add(mainLayout);
    }

    private void onConfirm(UserService userService, EmailService emailService, TextField usernameField) {
        try {
            validateUsernameInput(userService, usernameField.getValue());
            usernameField.setInvalid(false);

            new SuccessNotification(getTranslation("notification.reset.link.sent"), NotificationType.LONG).open();
            emailService.sendEmail(userService.getUser(userService.getIdByUsername(usernameField.getValue())), EmailType.NEWPASSOWRD);
        } catch (Exception ex) {
            usernameField.setErrorMessage(ex.getMessage());
            usernameField.setInvalid(true);
        }
    }

    private void validateUsernameInput(UserService userService, String input) throws ValidationException {
        if (input.isEmpty()) throw new ValidationException(getTranslation("exception.empty.field"));

        Collection<String> users = userService.getUsernames();
        if (!users.contains(input)) throw new ValidationException(getTranslation("exception.user.not.exist"));
    }
}
