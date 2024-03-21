package org.jk.esked.app.frontend.components.fields;

import org.apache.commons.lang3.StringUtils;
import org.jk.esked.app.backend.model.entities.User;
import org.jk.esked.app.backend.model.exceptions.ValidationException;
import org.jk.esked.app.backend.model.types.EmailType;
import org.jk.esked.app.backend.model.types.FieldType;
import org.jk.esked.app.backend.model.types.NotificationType;
import org.jk.esked.app.backend.services.utilities.EmailService;
import org.jk.esked.app.backend.services.UserService;
import org.jk.esked.app.frontend.components.other.SuccessNotification;

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

        updateMainValue(userService.findEmailById(userId));
    }

    @Override
    protected void validateInput(String input) throws ValidationException {
        if (StringUtils.isBlank(input))
            throw new ValidationException(getTranslation("exception.email.field.empty"));

        if (!input.contains("@"))
            throw new ValidationException(getTranslation("exception.non.email.syntax"));

        Collection<String> emails = userService.findAllRegisteredEmails();
        if (emails.stream().anyMatch(s -> s.equals(input))) {
            throw new ValidationException(getTranslation("exception.email.taken"));
        }
    }

    @Override
    protected void commitInput(String input) throws Exception {
        User user = userService.findById(userId);
        user.setEmail(input);
        new SuccessNotification(getTranslation("notification.email.link.sent"), NotificationType.SHORT).open();
        emailService.sendEmail(user, EmailType.NEWEMAIL);
        updateMainValue(input);
    }
}
