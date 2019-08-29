package org.jk.eSked.components.settingsFields;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import org.jk.eSked.model.User;
import org.jk.eSked.services.users.UserService;

import java.util.UUID;

public class MyPasswordField extends HorizontalLayout {
    private PasswordField textField;
    private Button button;

    public MyPasswordField(UUID userId, UserService userService) {
        textField = new PasswordField("Hasło");
        textField.setReadOnly(true);

        button = new Button("Zmień");

        textField.setWidth("80%");
        button.setWidth("20%");
        button.getStyle().set("margin-top", "auto");
        button.addClickListener(buttonClickEvent -> onClick(userId, userService));
        add(textField, button);
    }

    private void onClick(UUID userId, UserService userService) {
        textField.setInvalid(false);
        remove(button);
        Button button1 = new Button("Potwierdź");
        button1.getStyle().set("margin-top", "auto");
        button1.setWidth("50%");
        add(button1);
        textField.clear();
        textField.setReadOnly(false);
        textField.setPlaceholder("Nowe Hasło");
        button1.addClickListener(buttonClickEvent1 -> {
            if (!textField.getValue().equals("")) {
                textField.setInvalid(false);
                userService.changePassword(userId, User.encodePassword(textField.getValue()));
                Notification notification = new Notification("Hasło zostało zmienione!", 5000, Notification.Position.TOP_END);
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                notification.open();
            } else {
                textField.setErrorMessage("Pole nie może być puste");
                textField.setInvalid(true);
            }
            textField.setReadOnly(true);
            remove(button1);
            add(button);
        });
    }
}
