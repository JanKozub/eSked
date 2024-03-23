package org.jk.esked.app.frontend.views.admin.users;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.jk.esked.app.backend.model.entities.User;
import org.jk.esked.app.backend.services.*;
import org.jk.esked.app.backend.services.utilities.EmailService;
import org.jk.esked.app.backend.services.utilities.TimeService;
import org.jk.esked.app.frontend.components.admin.InfoBox;
import org.jk.esked.app.frontend.components.buttons.RedButton;
import org.jk.esked.app.frontend.components.events.EventGrid;
import org.jk.esked.app.frontend.components.fields.EmailField;
import org.jk.esked.app.frontend.components.fields.GroupCodeField;
import org.jk.esked.app.frontend.components.fields.MyPasswordField;
import org.jk.esked.app.frontend.components.fields.NameField;
import org.jk.esked.app.frontend.components.schedule.ScheduleGrid;
import org.jk.esked.app.frontend.views.MainLayout;

import java.time.LocalDate;
import java.util.UUID;

@Route(value = "user/:id", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class UserView extends VerticalLayout implements BeforeEnterObserver, HasDynamicTitle {
    private final ScheduleEntryService scheduleEntryService;
    private final UserService userService;
    private final EventService eventService;
    private final HourService hourService;
    private final EmailService emailService;
    private final GroupService groupService;
    private final MessageService messageService;

    public UserView(ScheduleEntryService scheduleEntryService, UserService userService, EventService eventService, HourService hourService, EmailService emailService, GroupService groupService, MessageService messageService) {
        this.scheduleEntryService = scheduleEntryService;
        this.userService = userService;
        this.eventService = eventService;
        this.hourService = hourService;
        this.emailService = emailService;
        this.groupService = groupService;
        this.messageService = messageService;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        User user = userService.findById(UUID.fromString(event.getRouteParameters().get("id").orElse("")));

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

        Button deleteButton = new RedButton(getTranslation("settings.tab.delete"), e -> {
            userService.deleteUser(user.getId());
            UI.getCurrent().navigate("admin");
        });

        addClassName("user-view");
        add(id, username, password, email, groupCode, darkTheme, scheduleHours, synWGroup, createdDate,
                lastLoggedDate, scheduleLabel, scheduleGrid, eventsLabel, eventGrid, deleteButton);
    }

    @Override
    public String getPageTitle() {
        return getTranslation("user");
    }
}
