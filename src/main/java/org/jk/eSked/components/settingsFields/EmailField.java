package org.jk.eSked.components.settingsFields;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import org.jk.eSked.services.users.UserService;

import java.util.Collection;
import java.util.UUID;

public class EmailField extends SettingsTextField {

    public EmailField(UUID userId, UserService userService) {
        textField.setLabel("Email");
        textField.setValue(userService.getEmail(userId));
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
        textField.setPlaceholder("Nowy emial");
        button1.addClickListener(buttonClickEvent1 -> {
            if (!textField.getValue().equals("")) {
                textField.setInvalid(false);
                if (textField.getValue().contains("@")) {
                    textField.setInvalid(false);
                    Collection<String> emails = userService.getEmails();
                    if (!emails.contains(textField.getValue())) {
                        textField.setInvalid(false);
                        userService.changeEmail(userId, textField.getValue());

                        Notification notification = new Notification("Zmieniono email na \"" + textField.getValue() + "\"", 5000, Notification.Position.TOP_END);
                        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                        notification.open();
                    } else {
                        textField.setErrorMessage("Ten email jest już zarejstorowany");
                        textField.setInvalid(true);
                    }
                } else {
                    textField.setErrorMessage("Wprowadzony tekst nie jest email'em");
                    textField.setInvalid(true);
                }
            } else {
                textField.setErrorMessage("Pole z nowym emailem nie może być puste");
                textField.setInvalid(true);
            }
            textField.setValue(userService.getEmail(userId));
            textField.setReadOnly(true);
            remove(button1);
            add(button);
        });
    }
}
