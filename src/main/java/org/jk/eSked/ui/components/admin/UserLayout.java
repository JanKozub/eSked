package org.jk.eSked.ui.components.admin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.jk.eSked.backend.model.User;
import org.jk.eSked.backend.service.EmailService;
import org.jk.eSked.backend.service.TimeService;
import org.jk.eSked.backend.service.user.*;
import org.jk.eSked.ui.components.events.EventGrid;
import org.jk.eSked.ui.components.schedule.ScheduleGrid;
import org.jk.eSked.ui.components.settings.fields.EmailField;
import org.jk.eSked.ui.components.settings.fields.GroupCodeField;
import org.jk.eSked.ui.components.settings.fields.MyPasswordField;
import org.jk.eSked.ui.components.settings.fields.NameField;

import java.time.LocalDate;

public class UserLayout extends VerticalLayout {
    public UserLayout(User user, UserService userService, EmailService emailService, GroupService groupService,
                      ScheduleService scheduleService, EventService eventService, HoursService hoursService) {
        InfoBox id = new InfoBox("ID: " + user.getId().toString());
        NameField username = new NameField(user.getId(), userService, emailService);
        MyPasswordField password = new MyPasswordField(user.getId(), userService, emailService);
        EmailField email = new EmailField(user.getId(), userService, emailService);
        GroupCodeField groupCode = new GroupCodeField(user.getId(), userService, groupService);

        InfoBox darkTheme = new InfoBox(getTranslation("schedule.theme.dark") + ": " + user.isDarkTheme());
        InfoBox scheduleHours = new InfoBox(getTranslation("schedule.hours") + ": " + user.isScheduleHours());
        InfoBox synWGroup = new InfoBox(getTranslation("user.sync.group") + ": " + user.isEventsSyn());
        InfoBox createdDate = new InfoBox(getTranslation("user.created.date") + ": " + TimeService.InstantToLocalDateTime(user.getCreatedDate()));
        InfoBox lastLoggedDate = new InfoBox(getTranslation("user.last.logged") + ": " + TimeService.InstantToLocalDateTime(user.getLastLoggedDate()));

        Label scheduleLabel = new Label(getTranslation("page.schedule"));
        VerticalLayout scheduleGrid = new ScheduleGrid(scheduleService, eventService, userService, hoursService);
        Label eventsLabel = new Label(getTranslation("page.events"));
        EventGrid eventGrid = new EventGrid(scheduleService, eventService, LocalDate.now());

        Button deleteButton = new Button(getTranslation("settings.tab.delete.acc"), e -> {
            userService.deleteUser(user.getId());
            UI.getCurrent().navigate("admin");
        });
        deleteButton.getStyle().set("color", "red");
        deleteButton.setWidth("100%");

        add(id, username, password, email, groupCode, darkTheme, scheduleHours, synWGroup, createdDate,
                lastLoggedDate, scheduleLabel, scheduleGrid, eventsLabel, eventGrid, deleteButton);
        setAlignItems(Alignment.CENTER);
    }
}
