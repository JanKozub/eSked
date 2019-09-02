package org.jk.eSked.components.settingsFields;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import org.apache.commons.lang3.StringUtils;
import org.jk.eSked.services.users.UserService;

import javax.validation.ValidationException;
import java.util.UUID;

public class GroupCodeField extends SettingsTextField {
    private UUID userId;
    private UserService userService;

    public GroupCodeField(UUID userId, UserService userService) {
        super("Kod grupy", "Nowy kod");
        this.userId = userId;
        this.userService = userService;
        int code = userService.getGroupCode(userId);
        if (code == 0) setValue("Brak");
        else setValue(Integer.toString(code));
    }

    @Override
    protected void validateInput(String input) {
        if (StringUtils.isBlank(input))
            throw new ValidationException("Pole z Kodem nie może być puste");

        if (!textField.getValue().matches("[0-9]+"))
            throw new ValidationException("Kod nie może zawierać liter");

        if (textField.getValue().length() != 4)
            throw new ValidationException("Kod musi zawierać 4 cyfry");
    }

    @Override
    protected void commitInput(String input) {
        userService.setGroupCode(userId, Integer.parseInt(textField.getValue()));
        Notification notification = new Notification("Kod został zmieniony na \"" + textField.getValue() + "\"", 5000, Notification.Position.TOP_END);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.open();

        completeEdit();
    }
}