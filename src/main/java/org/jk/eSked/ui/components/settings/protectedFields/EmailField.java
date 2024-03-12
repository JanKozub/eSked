package org.jk.eSked.ui.components.settings.protectedFields;

import com.vaadin.flow.server.VaadinSession;
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
import java.util.Collection;
import java.util.Locale;
import java.util.UUID;

public class EmailField extends NewSettingsField {
    private final static Locale locale = VaadinSession.getCurrent().getLocale();
    private final UUID userId;
    private final UserService userService;
    private final EmailService emailService;
    private final MessagesService messagesService;

    public EmailField(UserService userService, EmailService emailService, MessagesService messagesService) {
        super("Email", "Nowy Email");
        this.userId = SessionService.getUserId();
        this.userService = userService;
        this.emailService = emailService;
        this.messagesService = messagesService;

        updateMainValue(userService.getEmail(userId));
    }

    @Override
    protected void validateInput(String input) {
        if (StringUtils.isBlank(input))
            throw new ValidationException(getTranslation(locale, "exception_email_field_empty"));

        if (!input.contains("@"))
            throw new ValidationException(getTranslation(locale, "exception_non_email_syntax"));

        Collection<String> emails = userService.getEmails();
        if (emails.stream().anyMatch(s -> s.equals(input))) {
            throw new ValidationException(getTranslation(locale, "exception_email_taken"));
        }
    }

    @Override
    protected void commitInput(String input) throws Exception {
        User user = userService.getUser(userId);
        user.setEmail(input);
//        if (needConfirm) {//TODO FIX??
//            emailService.sendEmail(user, EmailType.NEWEMAIL);
//            new SuccessNotification("Link do zmiany email został wysłany na nowy email", NotificationType.SHORT).open();
//        }
        userService.setEmail(userId, input);
        new SuccessNotification(getTranslation(locale, "notification_email_changed_to") + " \"" + input + "\"", NotificationType.SHORT).open();
        messagesService.addMessageForUser(new Message(
                userId,
                messagesService.generateMessageId(),
                Instant.now().toEpochMilli(),
                getTranslation(locale, "message_email_changed") + " " + "\"" + input + "\"",
                false
        ));
    }
}
