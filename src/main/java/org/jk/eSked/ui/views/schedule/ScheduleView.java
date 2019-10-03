package org.jk.eSked.ui.views.schedule;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.backend.service.user.EventService;
import org.jk.eSked.backend.service.user.HoursService;
import org.jk.eSked.backend.service.user.ScheduleService;
import org.jk.eSked.backend.service.user.UserService;
import org.jk.eSked.ui.MenuView;
import org.jk.eSked.ui.components.schedule.ScheduleGrid;

@Route(value = "schedule", layout = MenuView.class)
@PWA(name = "eSked", shortName = "Schedule app", iconPath = "META-INF/resources/icons/icon.png", description = "Schedule app for students")
@PageTitle("Plan")
public class ScheduleView extends HorizontalLayout {
    public ScheduleView(ScheduleService scheduleService, EventService eventService, UserService userService, HoursService hoursService) {
        VerticalLayout scheduleGrid = new ScheduleGrid(scheduleService, eventService, userService, hoursService, SessionService.getUserId());
        scheduleGrid.setSizeFull();
        setSizeFull();
        add(scheduleGrid);
    }
}