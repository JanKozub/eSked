package org.jk.esked.app.frontend.components.settings;

import com.vaadin.flow.component.formlayout.FormLayout;
import org.jk.esked.app.backend.model.types.SettingsTabType;
import org.jk.esked.app.backend.services.UserService;
import org.jk.esked.app.backend.services.utilities.EmailService;
import org.jk.esked.app.frontend.components.fields.EmailField;
import org.jk.esked.app.frontend.components.fields.MyPasswordField;
import org.jk.esked.app.frontend.components.fields.NameField;

import java.util.UUID;

public class AccountTab extends SettingsTab {
    public AccountTab(UUID userId, UserService userService, EmailService emailService) {
        super(SettingsTabType.ACCOUNT);

        FormLayout accountForm = new FormLayout();
        accountForm.add(new NameField(userId, userService, emailService));
        accountForm.add(new MyPasswordField(userId, userService, emailService));
        accountForm.add(new EmailField(userId, userService, emailService));

        add(accountForm);
    }
}
