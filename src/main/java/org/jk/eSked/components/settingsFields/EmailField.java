package org.jk.eSked.components.settingsFields;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import org.apache.commons.lang3.StringUtils;
import org.jk.eSked.services.users.UserService;

import javax.validation.ValidationException;
import java.util.Collection;
import java.util.UUID;

public class EmailField extends SettingsTextField {

    private UUID userId;
    private UserService userService;

    public EmailField(UUID userId, UserService userService) {
        super("Email", "Nowy Email");
        this.userId = userId;
        this.userService = userService;
        setValue(userService.getEmail(userId));
    }

    @Override
    protected void validateInput(String input) {
        if (StringUtils.isBlank(input))
            throw new ValidationException("Pole z emailem nie może być puste");

        Collection<String> emails = userService.getEmails();
        if (emails.contains(textField.getValue())) {
            throw new ValidationException("Taki email jest juz zarejstrowyny");
        }

        if (!input.contains("@")) {
            throw new ValidationException("Podany tekst nie jest emailem");
        }
    }

    @Override
    protected void commitInput(String input) {
        userService.changeEmail(userId, textField.getValue());

        Notification notification = new Notification("Zmieniono email na \"" + textField.getValue() + "\"", 5000, Notification.Position.TOP_END);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.open();

        completeEdit();
    }
}