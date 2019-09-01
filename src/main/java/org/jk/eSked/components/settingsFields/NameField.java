package org.jk.eSked.components.settingsFields;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import org.apache.commons.lang3.StringUtils;
import org.jk.eSked.services.users.UserService;

import javax.validation.ValidationException;
import java.util.Collection;
import java.util.UUID;

public class NameField extends SettingsTextField {
    private UUID userId;
    private UserService userService;

    public NameField(UUID userId, UserService userService) {
        super("Nazwa", "Nowa nazwa");
        this.userId = userId;
        this.userService = userService;
        setValue(userService.getUsername(userId));
    }

    @Override
    protected void validateInput(String input) {
        if (StringUtils.isBlank(input))
            throw new ValidationException("Pole z nazwą nie może być puste");

        Collection<String> usernames = userService.getUsernames();
        if (usernames.contains(textField.getValue())) {
            throw new ValidationException("Użytkownik z taką nazwą istnieje");
        }
    }

    @Override
    protected void commitInput(String input) {
        userService.changeUsername(userId, input);

        Notification notification = new Notification("Zmieniono nazwę na \"" + textField.getValue() + "\"", 5000, Notification.Position.TOP_END);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.open();

        completeEdit();
    }
}
