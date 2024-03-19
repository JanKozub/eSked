package org.jk.esked.app.frontend.components.settings.tabs;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.server.VaadinSession;
import org.jk.esked.app.backend.model.types.SettingsTabType;
import org.jk.esked.app.backend.services.UserService;

import java.util.UUID;

public class DeleteTab extends SettingsTab {
    public DeleteTab(UUID userId, UserService userService) {
        super(SettingsTabType.DELETE);

        Button deleteButton = new Button(getTranslation("settings.tab.delete"));
        deleteButton.getStyle().set("color", "red");
        deleteButton.addClickListener(buttonClickEvent -> {
            Dialog dialog = new Dialog();
            Button button = new Button(getTranslation("confirm"));
            button.getStyle().set("color", "red");
            button.setWidth("100%");
            button.addClickListener(buttonClickEvent1 -> {
                userService.deleteUser(userId);
                UI.getCurrent().navigate("login");
                VaadinSession.getCurrent().close();
            });
            dialog.add(button);
            dialog.open();
        });
        deleteButton.setWidth("100%");

        add(deleteButton);
    }
}