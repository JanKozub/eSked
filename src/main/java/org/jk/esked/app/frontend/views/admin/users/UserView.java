package org.jk.esked.app.frontend.views.admin.users;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.RolesAllowed;
import org.jk.esked.app.backend.services.*;
import org.jk.esked.app.backend.services.utilities.EmailService;
import org.jk.esked.app.frontend.components.admin.UserLayout;
import org.jk.esked.app.frontend.views.MainLayout;

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
        UUID userId = UUID.fromString(event.getRouteParameters().get("id").orElse(""));

        add(new UserLayout(userService.findById(userId), userService, emailService,
                groupService, scheduleEntryService, eventService, hourService, messageService));
    }

    @Override
    public String getPageTitle() {
        return getTranslation("user");
    }
}
