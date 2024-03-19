package org.jk.esked.app.frontend.components.settings.fields;

import org.apache.commons.lang3.StringUtils;
import org.jk.esked.app.backend.model.exceptions.ValidationException;
import org.jk.esked.app.backend.model.types.EmailType;
import org.jk.esked.app.backend.model.types.FieldType;
import org.jk.esked.app.backend.model.types.NotificationType;
import org.jk.esked.app.backend.services.EmailService;
import org.jk.esked.app.backend.services.UserService;
import org.jk.esked.app.frontend.components.SuccessNotification;

import java.util.UUID;

public class MyPasswordField extends SettingsField {
    private final UUID userId;
    private final UserService userService;
    private final EmailService emailService;

    public MyPasswordField(UUID userId, UserService userService, EmailService emailService) {
        super(FieldType.PASSWORD);
        this.userId = userId;
        this.userService = userService;
        this.emailService = emailService;
    }

    @Override
    protected void validateInput(String input) throws ValidationException {
        if (StringUtils.isBlank(input))
            throw new ValidationException(getTranslation("exception.empty.field"));
    }

    @Override
    protected void commitInput(String input) throws Exception{
        new SuccessNotification(getTranslation("notification.reset.link.sent"), NotificationType.SHORT).open();
        emailService.sendEmail(userService.getUserById(userId), EmailType.NEWPASSOWRD);
    }
}
