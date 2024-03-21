package org.jk.esked.app.frontend.components.settings;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.server.VaadinSession;
import org.jk.esked.app.backend.model.types.SettingsTabType;
import org.jk.esked.app.backend.services.UserService;
import org.jk.esked.app.frontend.components.other.RedButton;

import java.util.UUID;

public class DeleteTab extends SettingsTab {
    public DeleteTab(UUID userId, UserService userService) {
        super(SettingsTabType.DELETE);

        Button deleteButton = new RedButton(getTranslation("settings.tab.delete"), c -> {
            Dialog dialog = new Dialog();
            Button button = new RedButton(getTranslation("confirm"), e -> {
                userService.deleteUser(userId);
                UI.getCurrent().navigate("login");
                VaadinSession.getCurrent().close();
            });
            button.setWidth("100%");
            dialog.add(button);
            dialog.open();
        });
        deleteButton.setWidth("100%");

        add(deleteButton);
    }
}
