package org.jk.eSked.ui.views.schedule;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.backend.model.User;
import org.jk.eSked.backend.service.EventService;
import org.jk.eSked.backend.service.HoursService;
import org.jk.eSked.backend.service.ScheduleService;
import org.jk.eSked.backend.service.UserService;
import org.jk.eSked.ui.MenuView;
import org.jk.eSked.ui.components.schedule.ScheduleGrid;

@Route(value = "schedule", layout = MenuView.class)
@PWA(name = "eSked", shortName = "Schedule app", iconPath = "META-INF/resources/icons/icon.png", description = "Schedule app for students")
@PageTitle("Plan")
public class ScheduleView extends HorizontalLayout {
    public ScheduleView(ScheduleService scheduleService, EventService eventService, UserService userService, HoursService hoursService) {
        VerticalLayout scheduleGrid = new ScheduleGrid(scheduleService, eventService, userService, hoursService, VaadinSession.getCurrent().getAttribute(User.class).getId());
        add(scheduleGrid);
    }
}