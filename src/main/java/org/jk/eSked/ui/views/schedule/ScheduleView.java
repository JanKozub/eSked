package org.jk.eSked.ui.views.schedule;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.app.LoginService;
import org.jk.eSked.backend.model.User;
import org.jk.eSked.backend.service.EventService;
import org.jk.eSked.backend.service.HoursService;
import org.jk.eSked.backend.service.ScheduleService;
import org.jk.eSked.backend.service.UserService;
import org.jk.eSked.ui.MenuView;
import org.jk.eSked.ui.components.schedule.ScheduleGrid;

@Route(value = "schedule", layout = MenuView.class)
@PageTitle("Plan")
public class ScheduleView extends HorizontalLayout {
    public ScheduleView(LoginService loginService, ScheduleService scheduleService, EventService eventService, UserService userService, HoursService hoursService) {
        if (loginService.checkIfUserIsLogged()) {
            VerticalLayout scheduleGrid = new ScheduleGrid(scheduleService, eventService, userService, hoursService, VaadinSession.getCurrent().getAttribute(User.class).getId());
            add(scheduleGrid);
        }
    }
}