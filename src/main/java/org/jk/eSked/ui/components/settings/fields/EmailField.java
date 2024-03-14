package org.jk.eSked.ui.components.settings.fields;

import org.apache.commons.lang3.StringUtils;
import org.jk.eSked.backend.model.User;
import org.jk.eSked.backend.model.types.EmailType;
import org.jk.eSked.backend.model.types.FieldType;
import org.jk.eSked.backend.model.types.NotificationType;
import org.jk.eSked.backend.service.EmailService;
import org.jk.eSked.backend.service.user.UserService;
import org.jk.eSked.ui.components.myComponents.SuccessNotification;

import javax.validation.ValidationException;
import java.util.Collection;
import java.util.UUID;

public class EmailField extends SettingsField {
    private final UUID userId;
    private final UserService userService;
    private final EmailService emailService;

    public EmailField(UUID userId, UserService userService, EmailService emailService) {
        super(FieldType.EMAIL);
        this.userId = userId;
        this.userService = userService;
        this.emailService = emailService;

        updateMainValue(userService.getEmail(userId));
    }

    @Override
    protected void validateInput(String input) {
        if (StringUtils.isBlank(input))
            throw new ValidationException(getTranslation("exception.email.field.empty"));

        if (!input.contains("@"))
            throw new ValidationException(getTranslation("exception.non.email.syntax"));

        Collection<String> emails = userService.getEmails();
        if (emails.stream().anyMatch(s -> s.equals(input))) {
            throw new ValidationException(getTranslation("exception.email.taken"));
        }
    }

    @Override
    protected void commitInput(String input) throws Exception {
        User user = userService.getUser(userId);
        user.setEmail(input);
        new SuccessNotification(getTranslation("notification.email.link.sent"), NotificationType.SHORT).open();
        emailService.sendEmail(user, EmailType.NEWEMAIL);
        updateMainValue(input);
    }
}
