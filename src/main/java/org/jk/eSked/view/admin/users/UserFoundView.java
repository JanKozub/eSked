package org.jk.eSked.view.admin.users;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.jk.eSked.components.EventGrid;
import org.jk.eSked.components.schedule.ScheduleGrid;
import org.jk.eSked.components.settingsFields.EmailField;
import org.jk.eSked.components.settingsFields.MyPasswordField;
import org.jk.eSked.components.settingsFields.NameField;
import org.jk.eSked.model.User;
import org.jk.eSked.services.LoginService;
import org.jk.eSked.services.events.EventService;
import org.jk.eSked.services.groups.GroupsService;
import org.jk.eSked.services.hours.HoursService;
import org.jk.eSked.services.schedule.ScheduleService;
import org.jk.eSked.services.users.UserService;

class UserFoundView extends VerticalLayout {

    UserFoundView(LoginService loginService, ScheduleService scheduleService, EventService eventService, UserService userService, GroupsService groupsService, HoursService hoursService, User user) {

        if (loginService.checkIfUserIsLogged()) {
            if (loginService.checkIfUserIsLoggedAsAdmin()) {

                Label mainLabel = new Label("Dane");

                NameField nameField = new NameField(user.getId(), userService);
                nameField.setWidth("50%");

                MyPasswordField myPasswordField = new MyPasswordField(user.getId(), userService);
                myPasswordField.setWidth("50%");

                EmailField emailField = new EmailField(user.getId(), userService);
                emailField.setWidth("50%");

                InfoBox idLayout = new InfoBox("ID Użytkownika: ", user.getId().toString());
                InfoBox groupCodeLayout = new InfoBox("Kod grupy: ", Integer.toString(user.getGroupCode()));
                InfoBox scheduleHoursLayout = new InfoBox("Godziny w planie:", Boolean.toString(user.isScheduleHours()));
                InfoBox lastLoggedLayout = new InfoBox("Ostatnio zalogowany: ", user.getLastLoggedDate().toString());
                InfoBox accountCreatedAtLayout = new InfoBox("Konto utwożone dnia: ", user.getCreatedDate().toString());

                Label scheduleLabel = new Label("Plan");
                VerticalLayout scheduleGrid = new ScheduleGrid(scheduleService, eventService, userService, groupsService, hoursService, user.getId());
                Label eventsLabel = new Label("Wydarzenia");
                VerticalLayout eventGrid = new EventGrid(scheduleService, eventService, user.getId());

                Button deleteButton = new Button("Usuń Konto", e -> {
                    userService.deleteUser(user.getId());
                    UI.getCurrent().navigate("admin");
                });
                deleteButton.getStyle().set("color", "red");
                deleteButton.setWidth("50%");

                setAlignItems(Alignment.CENTER);

                add(mainLabel, nameField, myPasswordField, emailField, idLayout, groupCodeLayout, scheduleHoursLayout, lastLoggedLayout,
                        accountCreatedAtLayout, scheduleLabel, scheduleGrid, eventsLabel, eventGrid, deleteButton);
            }
        }
    }

    private static class InfoBox extends HorizontalLayout {
        private InfoBox(String name, String data) {
            TextField id = new TextField();
            id.setValue(name + data);
            id.setReadOnly(true);
            id.setWidth("100%");
            add(id);
            setVerticalComponentAlignment(Alignment.CENTER, id);
            setWidth("50%");
        }
    }
}