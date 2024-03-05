package org.jk.eSked.ui.views.schedule;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.backend.service.user.EventService;
import org.jk.eSked.backend.service.user.HoursService;
import org.jk.eSked.backend.service.user.ScheduleService;
import org.jk.eSked.backend.service.user.UserService;
import org.jk.eSked.ui.MainLayout;
import org.jk.eSked.ui.components.schedule.ScheduleGrid;

@Route(value = "schedule", layout = MainLayout.class)
@PageTitle("Plan")
public class ScheduleView extends AppLayout {
    public ScheduleView(ScheduleService scheduleService, EventService eventService, UserService userService, HoursService hoursService) {
        SessionService.setAutoTheme();
        VerticalLayout scheduleGrid = new ScheduleGrid(scheduleService, eventService, userService, hoursService);

        setContent(scheduleGrid);
    }
}
