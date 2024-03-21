package org.jk.esked.app.frontend.components.fields;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import org.apache.commons.lang3.StringUtils;
import org.jk.esked.app.backend.model.exceptions.ValidationException;
import org.jk.esked.app.backend.model.types.EmailType;
import org.jk.esked.app.backend.model.types.FieldType;
import org.jk.esked.app.backend.model.types.NotificationType;
import org.jk.esked.app.backend.services.utilities.EmailService;
import org.jk.esked.app.backend.services.UserService;

import java.util.Collection;
import java.util.UUID;

public class NameField extends SettingsField {
    private final UUID userId;
    private final UserService userService;
    private final EmailService emailService;

    public NameField(UUID userId, UserService userService, EmailService emailService) {
        super(FieldType.USERNAME);
        this.userId = userId;
        this.userService = userService;
        this.emailService = emailService;

        updateMainValue(userService.findUsernameById(userId));
    }

    @Override
    protected void validateInput(String input) throws ValidationException {
        if (StringUtils.isBlank(input))
            throw new ValidationException(getTranslation("exception.empty.field"));

        Collection<String> usernames = userService.findAllUsernames();
        if (usernames.contains(input)) {
            throw new ValidationException(getTranslation("exception.user.exists"));
        }
    }

    @Override
    protected void commitInput(String input) throws Exception {
        userService.changeUsernameById(userId, input);

        Notification notification = new Notification(getTranslation("notification.username.changed") + " \"" + input + "\"",
                NotificationType.SHORT.getDuration(), Notification.Position.TOP_END);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.open();

        emailService.sendEmail(userService.findById(userId), EmailType.NEWUSERNAME);
        updateMainValue(userService.findUsernameById(userId));
    }
}
