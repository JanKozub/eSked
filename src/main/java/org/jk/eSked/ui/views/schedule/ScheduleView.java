package org.jk.eSked.ui.views.schedule;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.backend.service.user.EventService;
import org.jk.eSked.backend.service.user.HoursService;
import org.jk.eSked.backend.service.user.ScheduleService;
import org.jk.eSked.backend.service.user.UserService;
import org.jk.eSked.ui.components.menu.Menu;
import org.jk.eSked.ui.components.schedule.ScheduleGrid;

@Route(value = "schedule", layout = Menu.class)
@PageTitle("Plan")
public class ScheduleView extends HorizontalLayout {
    public ScheduleView(ScheduleService scheduleService, EventService eventService, UserService userService, HoursService hoursService) {
        Div scrollLayout = new Div();
        scrollLayout.setSizeFull();
        scrollLayout.getStyle().set("position", "absolute");
        scrollLayout.getStyle().set("top", "0px");
        scrollLayout.getStyle().set("left", "0px");
        scrollLayout.getStyle().set("margin-left", "0px");

        SessionService.setAutoTheme();
        VerticalLayout scheduleGrid = new ScheduleGrid(scheduleService, eventService, userService, hoursService, SessionService.getUserId());
        setSizeFull();
        add(scheduleGrid, scrollLayout);
    }
}
