package org.jk.eSked.components.settings;

import com.vaadin.flow.component.UI;
import org.apache.commons.lang3.StringUtils;
import org.jk.eSked.components.myImpl.SuccessNotification;
import org.jk.eSked.services.users.UserService;

import javax.validation.ValidationException;
import java.util.UUID;

public class GroupCodeField extends SettingsTextField {
    private final UUID userId;
    private final UserService userService;

    public GroupCodeField(UUID userId, UserService userService) {
        super("Kod grupy", "Nowy kod");
        this.userId = userId;
        this.userService = userService;
        int code = userService.getGroupCode(userId);
        if (code == 0) textField.setValue("Brak");
        else textField.setValue(Integer.toString(code));
    }

    @Override
    protected void validateInput(String input) {
        if (StringUtils.isBlank(input))
            throw new ValidationException("Pole z Kodem nie może być puste");

        if (!textField.getValue().matches("[0-9]+"))
            throw new ValidationException("Kod nie może zawierać spacji oraz liter");

        if (textField.getValue().length() != 4)
            throw new ValidationException("Kod musi zawierać 4 cyfry");
    }

    @Override
    protected void commitInput(String input) {
        userService.setGroupCode(userId, Integer.parseInt(textField.getValue()));
        UI.getCurrent().getPage().reload();
        SuccessNotification notification = new SuccessNotification("Kod został zmieniony na \"" + textField.getValue() + "\"");
        notification.open();
        completeEdit(Integer.toString(userService.getGroupCode(userId)));
    }
}