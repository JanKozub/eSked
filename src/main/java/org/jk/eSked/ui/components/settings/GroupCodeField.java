package org.jk.eSked.ui.components.settings;

import org.apache.commons.lang3.StringUtils;
import org.jk.eSked.backend.model.types.NotificationType;
import org.jk.eSked.backend.service.GroupService;
import org.jk.eSked.backend.service.UserService;
import org.jk.eSked.ui.components.myImpl.SuccessNotification;

import javax.validation.ValidationException;
import java.util.UUID;

public class GroupCodeField extends SettingsTextField {
    private final UUID userId;
    private final UserService userService;
    private GroupService groupService;

    public GroupCodeField(UUID userId, UserService userService, GroupService groupService) {
        super("Kod grupy", "Nowy kod");
        this.userId = userId;
        this.userService = userService;
        this.groupService = groupService;
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

        int value = Integer.parseInt(textField.getValue());
        if (groupService.getGroupCodes().stream().noneMatch(integer -> integer == value))
            throw new ValidationException("Grupa z takim kodem nie istnieje");
    }

    @Override
    protected void commitInput(String input) {
        userService.setGroupCode(userId, Integer.parseInt(textField.getValue()));
        new SuccessNotification("Kod został zmieniony na \"" + textField.getValue() + "\"", NotificationType.SHORT).open();
        completeEdit(Integer.toString(userService.getGroupCode(userId)));
    }
}