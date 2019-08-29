package org.jk.eSked.components.settingsFields;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import org.jk.eSked.services.users.UserService;

import java.util.UUID;

public class GroupCodeField extends SettingsTextField {
    public GroupCodeField(UUID userId, UserService userService) {
        textField.setLabel("Kod");
        String code;
        if (userService.getGroupCode(userId) == 0) code = "brak";
        else code = Integer.toString(userService.getGroupCode(userId));
        textField.setValue(code);
        button.addClickListener(buttonClickEvent -> onClick(userId, userService));
    }

    private void onClick(UUID userId, UserService userService) {
        remove(button);
        Button button1 = new Button("Potwierdź");
        button1.getStyle().set("margin-top", "auto");
        button1.setWidth("50%");
        add(button1);
        textField.setInvalid(false);
        textField.clear();
        textField.setReadOnly(false);
        textField.setPlaceholder("Nowy kod");
        button1.addClickListener(buttonClickEvent1 -> {
            if (!textField.getValue().equals("")) {
                textField.setInvalid(false);
                if (textField.getValue().matches("[0-9]+")) {
                    textField.setInvalid(false);
                    if (textField.getValue().length() == 4) {
                        textField.setInvalid(false);
                        userService.setGroupCode(userId, Integer.parseInt(textField.getValue()));
                        Notification notification = new Notification("Kod został zmieniony na \"" + textField.getValue() + "\"", 5000, Notification.Position.TOP_END);
                        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                        notification.open();
                    } else {
                        textField.setErrorMessage("Kod musi zawierać 4 liczby");
                        textField.setInvalid(true);
                    }
                } else {
                    textField.setErrorMessage("Kod musi być liczbą");
                    textField.setInvalid(true);
                }
            } else {
                textField.setErrorMessage("Pole nie może być puste");
                textField.setInvalid(true);
            }
            textField.setValue(Integer.toString(userService.getGroupCode(userId)));
            textField.setReadOnly(true);
            remove(button1);
            add(button);
        });
    }
}
