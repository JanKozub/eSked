package org.jk.eSked.ui.components.settings.fields;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import org.apache.commons.lang3.StringUtils;
import org.jk.eSked.backend.model.Message;
import org.jk.eSked.backend.model.types.EmailType;
import org.jk.eSked.backend.service.EmailService;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.backend.service.user.MessagesService;
import org.jk.eSked.backend.service.user.UserService;
import org.jk.eSked.ui.components.settings.NewSettingsField;

import javax.validation.ValidationException;
import java.time.Instant;
import java.util.Collection;
import java.util.UUID;

public class NameField extends NewSettingsField {
    private final UUID userId;
    private final UserService userService;
    private final EmailService emailService;
    private final MessagesService messagesService;

    public NameField(UserService userService, EmailService emailService, MessagesService messagesService) {
        super("Nazwa", "Nowa nazwa");
        this.userId = SessionService.getUserId();
        this.userService = userService;
        this.emailService = emailService;
        this.messagesService = messagesService;

        updateMainValue(userService.getUsername(userId));
    }

    @Override
    protected void validateInput(String input) {
        if (StringUtils.isBlank(input))
            throw new ValidationException("Pole z nazwą nie może być puste");

        Collection<String> usernames = userService.getUsernames();
        if (usernames.contains(input)) {
            throw new ValidationException("Użytkownik z taką nazwą istnieje");
        }
    }

    @Override
    protected void commitInput(String input) throws Exception {
        userService.setUsername(userId, input);

        Notification notification = new Notification("Zmieniono nazwę na \"" + input + "\"", 5000, Notification.Position.TOP_END);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.open();

        emailService.sendEmail(userService.getUser(userId), EmailType.NEWUSERNAME);

        messagesService.addMessageForUser(new Message(
                userId,
                messagesService.generateMessageId(),
                Instant.now().toEpochMilli(),
                "Twoja nazwa użytkownika została zmieniona na " + "\"" + input + "\"",
                false
        ));

        updateMainValue(userService.getUsername(userId));
    }
}
