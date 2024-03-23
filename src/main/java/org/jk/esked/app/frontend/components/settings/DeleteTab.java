package org.jk.esked.app.frontend.components.settings;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import org.jk.esked.app.backend.model.types.SettingsTabType;
import org.jk.esked.app.backend.security.SecurityService;
import org.jk.esked.app.backend.services.UserService;
import org.jk.esked.app.frontend.components.buttons.RedButton;

import java.util.UUID;

public class DeleteTab extends SettingsTab {
    public DeleteTab(UUID userId, UserService userService, SecurityService securityService) {
        super(SettingsTabType.DELETE);

        Button deleteButton = new RedButton(getTranslation("settings.tab.delete"), c -> {
            Button button = new RedButton(getTranslation("confirm"), e -> {
                securityService.logout();
                userService.deleteUser(userId);
            });
            button.setWidth("100%");
            new Dialog(button).open();
        });

        addClassName("delete-tab");
        add(deleteButton);
    }
}
