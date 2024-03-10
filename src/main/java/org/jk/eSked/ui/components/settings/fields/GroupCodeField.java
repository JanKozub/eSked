package org.jk.eSked.ui.components.settings.fields;

import com.vaadin.flow.component.button.Button;
import org.apache.commons.lang3.StringUtils;
import org.jk.eSked.backend.ApplicationContextHolder;
import org.jk.eSked.backend.model.Message;
import org.jk.eSked.backend.model.types.NotificationType;
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
    private final Button button;
    private final GroupCreator groupCreator;
    private final MessagesService messagesService;

    public GroupCodeField(UUID userId, UserService userService, GroupService groupService, Button button, GroupCreator groupCreator) {
        super("Kod grupy", "Nowy kod");
        this.userId = userId;
        this.userService = userService;
        this.groupService = groupService;
        this.button = button;
        this.groupCreator = groupCreator;
        this.messagesService = ApplicationContextHolder.getContext().getBean(MessagesService.class);
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
        setMainLayout(Integer.toString(userService.getGroupCode(userId)));
        button.setVisible(true);
        groupCreator.checkUserStatus();

        messagesService.addMessageForUser(new Message(
                userId,
                messagesService.generateMessageId(),
                Instant.now().toEpochMilli(),
                "Twój kod grupy został zmieniony na " + textField.getValue(),
                false
        ));
    }

    public void clear() {
        textField.clear();
    }
}
