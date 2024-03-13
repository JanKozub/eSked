package org.jk.eSked.ui.components.settings.fields;

import org.apache.commons.lang3.StringUtils;
import org.jk.eSked.backend.model.types.EmailType;
import org.jk.eSked.backend.model.types.FieldType;
import org.jk.eSked.backend.model.types.NotificationType;
import org.jk.eSked.backend.service.EmailService;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.backend.service.user.UserService;
import org.jk.eSked.ui.components.myComponents.SuccessNotification;
import org.jk.eSked.ui.components.settings.NewSettingsField;

import javax.validation.ValidationException;
import java.util.UUID;

public class MyPasswordField extends NewSettingsField {
    private final UUID userId;
    private final UserService userService;
    private final EmailService emailService;

    public MyPasswordField(UserService userService, EmailService emailService) {
        super(FieldType.PASSWORD);
        this.userId = SessionService.getUserId();
        this.userService = userService;
        this.emailService = emailService;
    }

    @Override
    protected void validateInput(String input) {
        if (StringUtils.isBlank(input))
            throw new ValidationException("Pole z hasłem nie może być puste");
    }

    @Override
    protected void commitInput(String input) throws Exception{
        new SuccessNotification("Link do zmiany hasła został wysłany na e-mail", NotificationType.SHORT).open();
        emailService.sendEmail(userService.getUser(userId), EmailType.NEWPASSOWRD);
    }
}
