package org.jk.eSked.ui.components.login;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.jk.eSked.backend.model.types.EmailType;
import org.jk.eSked.backend.model.types.NotificationType;
import org.jk.eSked.backend.service.EmailService;
import org.jk.eSked.backend.service.user.UserService;
import org.jk.eSked.ui.components.myImpl.SuccessNotification;

import javax.validation.ValidationException;
import java.util.Collection;

public class loginExceptionDialog extends Dialog {
    public loginExceptionDialog(UserService userService, EmailService emailService) {

        Label newUserLabel = new Label("Stwórz konto");
        newUserLabel.getStyle().set("font-weight", "bold");

        Button newUser = new Button("Utwórz", click -> {
            close();
            new NewUserDialog(userService, emailService).open();
        });
        newUser.setWidth("100%");

        Label passLabel = new Label("Odzyskaj Hasło");
        passLabel.getStyle().set("font-weight", "bold");

        TextField usernameField = new TextField();
        usernameField.setPlaceholder("Nazwa Użytkownika");
        usernameField.setWidth("60%");

        Button confirmButton = new Button("Wyślij email");
        confirmButton.addClickShortcut(Key.ENTER);
        confirmButton.setWidth("40%");
        confirmButton.addClickListener(event -> {
            try {
                validateUsernameInput(userService, usernameField.getValue());
                usernameField.setInvalid(false);

                emailService.sendEmail(userService.getUser(userService.getIdByUsername(usernameField.getValue())), EmailType.NEWPASSOWRD);

                new SuccessNotification("Link do zmiany hasła został wysłany na email", NotificationType.LONG).open();
            } catch (Exception ex) {
                usernameField.setErrorMessage(ex.getMessage());
                usernameField.setInvalid(true);
            }
        });

        HorizontalLayout passFieldLayout = new HorizontalLayout(usernameField, confirmButton);
        passFieldLayout.setWidth("100%");

        VerticalLayout mainLayout = new VerticalLayout(newUserLabel, newUser, passLabel, passFieldLayout);
        mainLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        add(mainLayout);
    }

    private void validateUsernameInput(UserService userService, String input) {
        if (input.isEmpty()) throw new ValidationException("Pole nie może być puste");

        Collection<String> users = userService.getUsernames();
        if (!users.contains(input)) throw new ValidationException("Użytkownik z taką nazwą nie istnieje");
    }
}