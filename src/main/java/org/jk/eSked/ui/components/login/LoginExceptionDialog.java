package org.jk.eSked.ui.components.login;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.backend.model.types.EmailType;
import org.jk.eSked.backend.model.types.NotificationType;
import org.jk.eSked.backend.service.EmailService;
import org.jk.eSked.backend.service.user.UserService;
import org.jk.eSked.ui.components.myComponents.SuccessNotification;

import javax.validation.ValidationException;
import java.util.Collection;
import java.util.Locale;

public class LoginExceptionDialog extends Dialog {
    private final static Locale locale = VaadinSession.getCurrent().getLocale();

    public LoginExceptionDialog(UserService userService, EmailService emailService) {

        Label newUserLabel = new Label(getTranslation(locale,"login_exception_label"));
        newUserLabel.getStyle().set("font-weight", "bold");

        Button newUser = new Button(getTranslation(locale,"login_exception_button"), click -> {
            close();
            new NewUserDialog(userService, emailService).open();
        });
        newUser.setWidth("100%");

        Label passLabel = new Label(getTranslation(locale,"login_exception_password"));
        passLabel.getStyle().set("font-weight", "bold");

        TextField usernameField = new TextField();
        usernameField.setPlaceholder(getTranslation(locale,"username"));
        usernameField.setWidth("60%");

        Button confirmButton = new Button(getTranslation(locale,"login_exception_email"));
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

            new SuccessNotification(getTranslation(locale,"login_exception_success"), NotificationType.LONG).open();
            emailService.sendEmail(userService.getUser(userService.getIdByUsername(usernameField.getValue())), EmailType.NEWPASSOWRD);
        } catch (Exception ex) {
            usernameField.setErrorMessage(ex.getMessage());
            usernameField.setInvalid(true);
        }
    }

    private void validateUsernameInput(UserService userService, String input) {
        if (input.isEmpty()) throw new ValidationException(getTranslation(locale,"exception_empty_field"));

        Collection<String> users = userService.getUsernames();
        if (!users.contains(input)) throw new ValidationException(getTranslation(locale, "exception_user_not_exist"));
    }
}
