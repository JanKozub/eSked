package org.jk.esked.app.frontend.components.settings.fields;

import org.apache.commons.lang3.StringUtils;
import org.jk.esked.app.backend.model.Message;
import org.jk.esked.app.backend.model.exceptions.ValidationException;
import org.jk.esked.app.backend.model.types.FieldType;
import org.jk.esked.app.backend.model.types.NotificationType;
import org.jk.esked.app.backend.services.GroupService;
import org.jk.esked.app.backend.services.MessageService;
import org.jk.esked.app.backend.services.UserService;
import org.jk.esked.app.frontend.components.SuccessNotification;

import java.util.UUID;

public class GroupCodeField extends SettingsField {
    private final UUID userId;
    private final UserService userService;
    private final GroupService groupService;
    private final MessageService messageService;

    public GroupCodeField(UUID userId, UserService userService, GroupService groupService, MessageService messageService) {
        super(FieldType.GROUP);
        this.userId = userId;
        this.userService = userService;
        this.groupService = groupService;
        this.messageService = messageService;

        int code = userService.getGroupCodeByUserId(userId);
        if (code == 0) updateMainValue(getTranslation("group.code.empty"));
        else updateMainValue(Integer.toString(code));
    }

    @Override
    protected void validateInput(String input) throws ValidationException {
        if (StringUtils.isBlank(input))
            throw new ValidationException(getTranslation("exception.empty.field"));

        if (!input.matches("[0-9]+"))
            throw new ValidationException(getTranslation("exception.code.syntax"));

        if (input.length() != 4)
            throw new ValidationException(getTranslation("exception.code.length"));

        int value = Integer.parseInt(input);
        if (groupService.getAllGroupCodes().stream().noneMatch(integer -> integer == value))
            throw new ValidationException(getTranslation("exception.group.not.exist"));
    }

    @Override
    protected void commitInput(String input) {
        userService.changeGroupCodeByUserId(userId, Integer.parseInt(input));
        new SuccessNotification(getTranslation("notification.code.changed") + " \"" + input + "\"", NotificationType.SHORT).open();
        updateMainValue(Integer.toString(userService.getGroupCodeByUserId(userId)));

        Message message = new Message();
        message.setUser(userService.getUserById(userId));
        message.setText(getTranslation("notification.code.changed") + " " + input);
        messageService.saveMessage(message);
    }
}
