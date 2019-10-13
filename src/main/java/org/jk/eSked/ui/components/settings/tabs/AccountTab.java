package org.jk.eSked.ui.components.settings.tabs;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import org.jk.eSked.ui.components.settings.fields.NameField;
import org.jk.eSked.ui.components.settings.protectedFields.EmailField;
import org.jk.eSked.ui.components.settings.protectedFields.MyPasswordField;

public class AccountTab extends SettingsTab {

    public AccountTab() {
        super(new Label("Użytkownik"));

        FormLayout accountForm = new FormLayout();
        accountForm.add(new NameField(userId, userService, emailService));
        accountForm.add(new MyPasswordField(userId, userService, emailService, true));
        accountForm.add(new EmailField(userId, userService, emailService, true));
        Button newEntries = new Button("Dodaj Przedmioty Do Planu",
                buttonClickEvent -> UI.getCurrent().navigate("schedule/new"));
        newEntries.getStyle().set("margin-top", "auto");
        accountForm.add(newEntries);
        add(accountForm);
    }
}
