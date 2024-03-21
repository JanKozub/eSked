package org.jk.esked.app.frontend.components.fields;

import org.apache.commons.lang3.StringUtils;
import org.jk.esked.app.backend.model.entities.Message;
import org.jk.esked.app.backend.model.entities.User;
import org.jk.esked.app.backend.model.exceptions.ValidationException;
import org.jk.esked.app.backend.model.types.FieldType;
import org.jk.esked.app.backend.model.types.NotificationType;
import org.jk.esked.app.backend.services.GroupService;
import org.jk.esked.app.backend.services.MessageService;
import org.jk.esked.app.backend.services.UserService;
import org.jk.esked.app.frontend.components.other.SuccessNotification;

public class GroupCodeField extends SettingsField {
    private final User user;
    private final UserService userService;
    private final GroupService groupService;
    private final MessageService messageService;

    public GroupCodeField(User user, UserService userService, GroupService groupService, MessageService messageService) {
        super(FieldType.GROUP);
        this.user = user;
        this.userService = userService;
        this.groupService = groupService;
        this.messageService = messageService;

        if (user.getGroupCode() == 0) updateMainValue(getTranslation("group.code.empty"));
        else updateMainValue(Integer.toString(user.getGroupCode()));
    }

    @Override
    protected void validateInput(String input) throws ValidationException {
        if (StringUtils.isBlank(input))
            throw new ValidationException(getTranslation("exception.empty.field"));

        if (!input.matches("[0-9]+"))
            throw new ValidationException(getTranslation("exception.code.syntax"));

        if (input.length() != 6)
            throw new ValidationException(getTranslation("exception.code.length"));

        int value = Integer.parseInt(input);
        if (groupService.findAllGroupCodes().stream().noneMatch(integer -> integer == value))
            throw new ValidationException(getTranslation("exception.group.not.exist"));
    }

    @Override
    protected void commitInput(String input) {
        userService.changeGroupCodeById(user.getId(), Integer.parseInt(input));
        new SuccessNotification(getTranslation("notification.code.changed") + " \"" + input + "\"", NotificationType.SHORT).open();
        updateMainValue(Integer.toString(userService.findGroupCodeById(user.getId())));

        Message message = new Message();
        message.setUser(user);
        message.setText(getTranslation("notification.code.changed") + " " + input);
        messageService.saveMessage(message);
    }
}
