package org.jk.eSked.ui.components.settings.tabs;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.backend.service.user.UserService;

public class DeleteTab extends SettingsTab {
    public DeleteTab(UserService userService, String title) {
        super(new Label(title));

        Button deleteButton = new Button(title);
        deleteButton.getStyle().set("color", "red");
        deleteButton.addClickListener(buttonClickEvent -> {
            Dialog dialog = new Dialog();
            Button button = new Button(getTranslation("confirm"));
            button.getStyle().set("color", "red");
            button.setWidth("100%");
            button.addClickListener(buttonClickEvent1 -> {
                userService.deleteUser(SessionService.getUserId());
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
