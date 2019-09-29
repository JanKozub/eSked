package org.jk.eSked.ui.components.settings;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.TextField;
import org.apache.commons.lang3.StringUtils;
import org.jk.eSked.backend.model.EmailType;
import org.jk.eSked.backend.service.EmailService;
import org.jk.eSked.backend.service.UserService;
import org.jk.eSked.ui.components.myImpl.LongSuccessNotification;
import org.jk.eSked.ui.components.myImpl.SuccessNotification;

import javax.validation.ValidationException;
import java.util.Collection;
import java.util.UUID;

public class EmailField extends SettingsTextField {

    private final UUID userId;
    private final UserService userService;
    private final EmailService emailService;
    private final boolean needConfirm;

    public EmailField(UUID userId, UserService userService, EmailService emailService, boolean needConfirm) {
        super("Email", "Nowy Email");
        this.userId = userId;
        this.userService = userService;
        this.emailService = emailService;
        this.needConfirm = needConfirm;
        textField.setValue(userService.getEmail(userId));
    }

    @Override
    protected void validateInput(String input) {
        if (StringUtils.isBlank(input))
            throw new ValidationException("Pole z emailem nie może być puste");

        Collection<String> emails = userService.getEmails();
        if (emails.contains(textField.getValue())) {
            throw new ValidationException("Taki email jest juz zarejstrowyny");
        }

        if (!input.contains("@")) {
            throw new ValidationException("Podany tekst nie jest emailem");
        }
    }

    @Override
    protected void commitInput(String input) throws Exception {
        TextField codeField = new TextField();
        Button button = new Button("Potwierdź");
        button.setWidth("40%");

        if (needConfirm) {

            emailService.sendEmail(userService.getUser(userId), EmailType.NEWEMAIL);

            new LongSuccessNotification("Link do zmiany email został wysłany na nowy email");

        } else {
            userService.changeEmail(userId, textField.getValue());

            SuccessNotification notification = new SuccessNotification("Zmieniono email na \"" + textField.getValue() + "\"");
            notification.open();

            completeEdit(userService.getEmail(userId));
        }
    }
}