package org.jk.eSked.components.settingsFields;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import org.jk.eSked.services.users.UserService;

import java.util.Collection;
import java.util.UUID;

public class NameField extends SettingsTextField {
    public NameField(UUID userId, UserService userService) {
        textField.setLabel("Nazwa");
        textField.setValue(userService.getUsername(userId));
        button.addClickListener(buttonClickEvent -> onClick(userId, userService));
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
        textField.setPlaceholder("Nowa nazwa");
        button1.addClickListener(buttonClickEvent1 -> {
            if (!textField.getValue().equals("")) {
                textField.setInvalid(false);
                Collection<String> usernames = userService.getUsernames();
                if (!usernames.contains(textField.getValue())) {
                    textField.setInvalid(false);
                    userService.changeUsername(userId, textField.getValue());

                    Notification notification = new Notification("Zmieniono nazwę na \"" + textField.getValue() + "\"", 5000, Notification.Position.TOP_END);
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    notification.open();
                } else {
                    textField.setErrorMessage("Użytkownik z taką nazwą istnieje");
                    textField.setInvalid(true);
                }
            } else {
                textField.setErrorMessage("Pole z nową nazwą nie może być puste");
                textField.setInvalid(true);
            }
            textField.setValue(userService.getUsername(userId));
            textField.setReadOnly(true);
            remove(button1);
            add(button);
        });
    }
}
