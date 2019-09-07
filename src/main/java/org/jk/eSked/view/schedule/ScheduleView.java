package org.jk.eSked.view.schedule;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.components.grids.ScheduleGrid;
import org.jk.eSked.model.User;
import org.jk.eSked.services.LoginService;
import org.jk.eSked.services.events.EventService;
import org.jk.eSked.services.hours.HoursService;
import org.jk.eSked.services.schedule.ScheduleService;
import org.jk.eSked.services.users.UserService;
import org.jk.eSked.view.MenuView;

@Route(value = "schedule", layout = MenuView.class)
@PageTitle("Plan")
public class ScheduleView extends HorizontalLayout{
    public ScheduleView(LoginService loginService, ScheduleService scheduleService, EventService eventService, UserService userService, HoursService hoursService) {
        if (loginService.checkIfUserIsLogged()) {
            VerticalLayout scheduleGrid = new ScheduleGrid(scheduleService, eventService, userService, hoursService, VaadinSession.getCurrent().getAttribute(User.class).getId());
            add(scheduleGrid);
        }
    }
}