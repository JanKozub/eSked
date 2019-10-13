package org.jk.eSked.ui.components.settings.tabs;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.server.VaadinSession;

public class DeleteTab extends SettingsTab {

    public DeleteTab() {
        super(new Label("Usuń konto"));

        Button deleteButton = new Button("Usuń konto");
        deleteButton.getStyle().set("color", "red");
        deleteButton.addClickListener(buttonClickEvent -> {
            Dialog dialog = new Dialog();
            Button button = new Button("Potwierdź");
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
