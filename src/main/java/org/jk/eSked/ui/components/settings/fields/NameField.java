package org.jk.eSked.ui.components.settings.fields;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import org.apache.commons.lang3.StringUtils;
import org.jk.eSked.backend.model.types.EmailType;
import org.jk.eSked.backend.service.EmailService;
import org.jk.eSked.backend.service.user.UserService;

import javax.validation.ValidationException;
import java.util.Collection;
import java.util.UUID;

public class NameField extends SettingsField {
    private final UUID userId;
    private final UserService userService;
    private final EmailService emailService;

    public NameField(UUID userId, UserService userService, EmailService emailService) {
        super("Nazwa", "Nowa nazwa");
        this.userId = userId;
        this.userService = userService;
        this.emailService = emailService;
        textField.setValue(userService.getUsername(userId));
    }

    @Override
    protected void validateInput(String input) {
        if (StringUtils.isBlank(input))
            throw new ValidationException("Pole z nazwą nie może być puste");

        Collection<String> usernames = userService.getUsernames();
        if (usernames.contains(textField.getValue())) {
            throw new ValidationException("Użytkownik z taką nazwą istnieje");
        }
    }

    @Override
    protected void commitInput(String input) throws Exception {
        String oldName = userService.getUsername(userId);
        userService.changeUsername(userId, input);

        Notification notification = new Notification("Zmieniono nazwę na \"" + textField.getValue() + "\"", 5000, Notification.Position.TOP_END);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.open();

        emailService.sendEmail(userService.getUser(userId), EmailType.NEWUSERNAME);

        setMainLayout(userService.getUsername(userId));
    }
}
