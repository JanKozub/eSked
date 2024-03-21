package org.jk.esked.app.frontend.components.admin;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.jk.esked.app.backend.model.entities.User;
import org.jk.esked.app.backend.services.*;
import org.jk.esked.app.backend.services.utilities.EmailService;
import org.jk.esked.app.backend.services.utilities.TimeService;
import org.jk.esked.app.frontend.components.events.EventGrid;
import org.jk.esked.app.frontend.components.schedule.ScheduleGrid;
import org.jk.esked.app.frontend.components.fields.EmailField;
import org.jk.esked.app.frontend.components.fields.GroupCodeField;
import org.jk.esked.app.frontend.components.fields.MyPasswordField;
import org.jk.esked.app.frontend.components.fields.NameField;

import java.time.LocalDate;

public class UserLayout extends VerticalLayout {
    public UserLayout(User user, UserService userService, EmailService emailService, GroupService groupService,
                      ScheduleEntryService scheduleEntryService, EventService eventService, HourService hourService, MessageService messageService) {
        InfoBox id = new InfoBox("ID: " + user.getId().toString());
        NameField username = new NameField(user.getId(), userService, emailService);
        MyPasswordField password = new MyPasswordField(user.getId(), userService, emailService);
        EmailField email = new EmailField(user.getId(), userService, emailService);
        GroupCodeField groupCode = new GroupCodeField(user, userService, groupService, messageService);

        InfoBox darkTheme = new InfoBox(getTranslation("schedule.theme.dark") + ": " + user.isDarkTheme());
        InfoBox scheduleHours = new InfoBox(getTranslation("schedule.hours") + ": " + user.isScheduleHours());
        InfoBox synWGroup = new InfoBox(getTranslation("user.sync.group") + ": " + user.isEventSync());
        InfoBox createdDate = new InfoBox(getTranslation("user.created.date") + ": " + TimeService.timestampToFormatedString(user.getCreatedTimestamp()));
        InfoBox lastLoggedDate = new InfoBox(getTranslation("user.last.logged") + ": " + TimeService.timestampToFormatedString(user.getLastLoggedTimestamp()));

        Text scheduleLabel = new Text(getTranslation("page.schedule"));
        VerticalLayout scheduleGrid = new ScheduleGrid(user, scheduleEntryService, eventService, userService, hourService);
        Text eventsLabel = new Text(getTranslation("page.events"));
        EventGrid eventGrid = new EventGrid(user.getId(), scheduleEntryService, eventService, LocalDate.now());

        Button deleteButton = new Button(getTranslation("settings.tab.delete"), e -> {
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
