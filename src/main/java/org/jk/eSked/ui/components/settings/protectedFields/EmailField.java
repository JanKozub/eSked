package org.jk.eSked.ui.components.settings.protectedFields;

import org.apache.commons.lang3.StringUtils;
import org.jk.eSked.backend.ApplicationContextHolder;
import org.jk.eSked.backend.model.Message;
import org.jk.eSked.backend.model.User;
import org.jk.eSked.backend.model.types.EmailType;
import org.jk.eSked.backend.model.types.NotificationType;
import org.jk.eSked.backend.service.EmailService;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.backend.service.user.MessagesService;
import org.jk.eSked.backend.service.user.UserService;
import org.jk.eSked.ui.components.myComponents.SuccessNotification;

import javax.validation.ValidationException;
import java.time.Instant;
import java.util.Collection;
import java.util.UUID;

public class EmailField extends ProtectedSettingsField {

    private final UUID userId;
    private final UserService userService;
    private final EmailService emailService;
    private final boolean needConfirm;
    private final MessagesService messagesService;

    public EmailField(UUID userId, UserService userService, EmailService emailService, boolean needConfirm) {
        super(userService, "Email", userService.getEmail(userId), "Nowy Email");
        this.userId = userId;
        this.userService = userService;
        this.emailService = emailService;
        this.needConfirm = needConfirm;
        this.messagesService = ApplicationContextHolder.getContext().getBean(MessagesService.class);
    }

    @Override
    protected void validateInput(String input) {
        if (StringUtils.isBlank(input))
            throw new ValidationException("Pole z emailem nie może być puste");

        if (!input.contains("@"))
            throw new ValidationException("Podany tekst nie jest emailem");

        Collection<String> emails = userService.getEmails();
        if (emails.stream().anyMatch(s -> s.equals(input))) {
            throw new ValidationException("Taki email jest juz zarejstrowyny");
        }
    }

    @Override
    protected void commitInput(String input) throws Exception {
        User user = userService.getUser(userId);
        user.setEmail(input);
        if (needConfirm) {
            emailService.sendEmail(user, EmailType.NEWEMAIL);
            new SuccessNotification("Link do zmiany email został wysłany na nowy email", NotificationType.SHORT).open();
        } else {
            userService.setEmail(userId, textField.getValue());
            new SuccessNotification("Zmieniono email na \"" + input + "\"", NotificationType.SHORT).open();
            messagesService.addMessageForUser(new Message(
                    userId,
                    messagesService.generateMessageId(),
                    Instant.now().toEpochMilli(),
                    "Twój email został zmieniony na " + "\"" + input + "\"",
                    false
            ));
        }
        setMainLayout(userService.getEmail(SessionService.getUserId()));
    }
}
