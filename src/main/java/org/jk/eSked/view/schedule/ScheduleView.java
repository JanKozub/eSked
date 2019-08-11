package org.jk.eSked.view.schedule;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.component.schedule.ScheduleGrid;
import org.jk.eSked.model.User;
import org.jk.eSked.services.TimeService;
import org.jk.eSked.services.events.EventService;
import org.jk.eSked.services.groups.GroupsService;
import org.jk.eSked.services.schedule.ScheduleService;
import org.jk.eSked.services.users.UserService;
import org.jk.eSked.services.LoginService;
import org.jk.eSked.view.menu.MenuView;

@Route(value = "schedule", layout = MenuView.class)
@PageTitle("Plan")
public class ScheduleView extends HorizontalLayout {

    public ScheduleView(LoginService loginService, ScheduleService scheduleService, EventService eventService, UserService userService, GroupsService groupsService, TimeService timeService) {
        if (loginService.checkIfUserIsLogged()) {
            VerticalLayout scheduleGrid = new ScheduleGrid(scheduleService, eventService, userService, groupsService, timeService, VaadinSession.getCurrent().getAttribute(User.class).getId());
            add(scheduleGrid);
        }
    }
}