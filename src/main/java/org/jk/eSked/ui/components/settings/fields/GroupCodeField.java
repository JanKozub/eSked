package org.jk.eSked.ui.components.settings.fields;

import org.apache.commons.lang3.StringUtils;
import org.jk.eSked.backend.ApplicationContextHolder;
import org.jk.eSked.backend.model.Message;
import org.jk.eSked.backend.model.types.FieldType;
import org.jk.eSked.backend.model.types.NotificationType;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.backend.service.user.GroupService;
import org.jk.eSked.backend.service.user.MessagesService;
import org.jk.eSked.backend.service.user.UserService;
import org.jk.eSked.ui.components.myComponents.SuccessNotification;

import javax.validation.ValidationException;
import java.time.Instant;
import java.util.UUID;

public class GroupCodeField extends SettingsField {
    private final UUID userId;
    private final UserService userService;
    private final GroupService groupService;
    private final MessagesService messagesService;

    public GroupCodeField(UserService userService, GroupService groupService) {
        super(FieldType.GROUP);
        this.userId = SessionService.getUserId();
        this.userService = userService;
        this.groupService = groupService;
        this.messagesService = ApplicationContextHolder.getContext().getBean(MessagesService.class);

        int code = userService.getGroupCode(userId);
        if (code == 0) updateMainValue(getTranslation("group.code.empty"));
        else updateMainValue(Integer.toString(code));
    }

    @Override
    protected void validateInput(String input) {
        if (StringUtils.isBlank(input))
            throw new ValidationException(getTranslation("exception.empty.field"));

        if (!input.matches("[0-9]+"))
            throw new ValidationException(getTranslation("exception.code.syntax"));

        if (input.length() != 4)
            throw new ValidationException(getTranslation("exception.code.length"));

        int value = Integer.parseInt(input);
        if (groupService.getGroupCodes().stream().noneMatch(integer -> integer == value))
            throw new ValidationException(getTranslation("exception.group.not.exist"));
    }

    @Override
    protected void commitInput(String input) {
        userService.setGroupCode(userId, Integer.parseInt(input));
        new SuccessNotification(getTranslation("notification.code.changed") + " \"" + input + "\"", NotificationType.SHORT).open();
        updateMainValue(Integer.toString(userService.getGroupCode(userId)));

        messagesService.addMessageForUser(new Message(
                userId,
                messagesService.generateMessageId(),
                Instant.now().toEpochMilli(),
                getTranslation("notification.code.changed") + " " + input,
                false
        ));
    }
}
