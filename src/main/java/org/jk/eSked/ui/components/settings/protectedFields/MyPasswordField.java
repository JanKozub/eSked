package org.jk.eSked.ui.components.settings.protectedFields;

import org.apache.commons.lang3.StringUtils;
import org.jk.eSked.backend.model.Message;
import org.jk.eSked.backend.model.User;
import org.jk.eSked.backend.model.types.NotificationType;
import org.jk.eSked.backend.service.EmailService;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.backend.service.user.MessagesService;
import org.jk.eSked.backend.service.user.UserService;
import org.jk.eSked.ui.components.myComponents.SuccessNotification;
import org.jk.eSked.ui.components.settings.NewSettingsField;

import javax.validation.ValidationException;
import java.time.Instant;
import java.util.UUID;

public class MyPasswordField extends NewSettingsField {
    private final UUID userId;
    private final UserService userService;
    private final EmailService emailService;
    private final MessagesService messagesService;

    public MyPasswordField(UserService userService, EmailService emailService, MessagesService messagesService) {
        super("Hasło", "Nowe Hasło");
        this.userId = SessionService.getUserId();
        this.userService = userService;
        this.emailService = emailService;
        this.messagesService = messagesService;
    }

    @Override
    protected void validateInput(String input) {
        if (StringUtils.isBlank(input))
            throw new ValidationException("Pole z hasłem nie może być puste");
    }

    @Override
    protected void commitInput(String input) throws Exception {
        User user = userService.getUser(userId);
        user.setPassword(User.encodePassword(input));
//        if (needConfirm) { //TODO FIX?
//            emailService.sendEmail(user, EmailType.NEWPASSOWRD);
//            new SuccessNotification("Link do zmiany hasła został wysłany na email", NotificationType.SHORT).open();
//        }

        userService.changePassword(userId, User.encodePassword(input));
        new SuccessNotification("Hasło zostało zmienione", NotificationType.SHORT).open();

        messagesService.addMessageForUser(new Message(
                userId,
                messagesService.generateMessageId(),
                Instant.now().toEpochMilli(),
                "Twoje hasło zostało zmienione",
                false
        ));
    }
}
