package org.jk.eSked.ui.components.settings.tabs;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import org.jk.eSked.backend.service.EmailService;
import org.jk.eSked.backend.service.user.MessagesService;
import org.jk.eSked.backend.service.user.UserService;
import org.jk.eSked.ui.components.settings.fields.NameField;
import org.jk.eSked.ui.components.settings.protectedFields.EmailField;
import org.jk.eSked.ui.components.settings.protectedFields.MyPasswordField;

import java.util.Locale;

public class AccountTab extends SettingsTab {
    public AccountTab(UserService userService, EmailService emailService, MessagesService messagesService, String title, Locale locale) {
        super(new Label(title));

        FormLayout accountForm = new FormLayout();
        accountForm.add(new NameField(userService, emailService, messagesService));
        accountForm.add(new MyPasswordField(userService, emailService, messagesService));
        accountForm.add(new EmailField(userService, emailService, messagesService));
        Button newEntries = new Button(getTranslation(locale, "settings_tab_add_entry"),
                buttonClickEvent -> UI.getCurrent().navigate("schedule/new"));
        newEntries.getStyle().set("margin-top", "auto");
        accountForm.add(newEntries);
        add(accountForm);
    }
}
