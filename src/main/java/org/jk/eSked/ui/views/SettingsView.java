package org.jk.eSked.ui.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.backend.service.EmailService;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.backend.service.user.GroupService;
import org.jk.eSked.backend.service.user.HoursService;
import org.jk.eSked.backend.service.user.UserService;
import org.jk.eSked.ui.MenuView;
import org.jk.eSked.ui.components.myImpl.Line;
import org.jk.eSked.ui.components.settings.SettingsTabs;

import java.util.UUID;

@Route(value = "settings", layout = MenuView.class)
@PageTitle("Ustawienia")
public class SettingsView extends VerticalLayout {
    public SettingsView(UserService userService, GroupService groupsService, HoursService hoursService, EmailService emailService) {
        UUID userId = SessionService.getUserId();

        setSizeFull();
        setAlignItems(Alignment.CENTER);

        SettingsTabs settingsTabs = new SettingsTabs(userService, groupsService, hoursService, emailService, userId);

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

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(settingsTabs.accountLayout(), settingsTabs.groupLayout(), settingsTabs.otherLayout(), new Line(), deleteButton);
        if (SessionService.isSessionMobile())
            verticalLayout.setWidth("100%");
        else verticalLayout.setWidth("50%");
        add(verticalLayout);
    }
}