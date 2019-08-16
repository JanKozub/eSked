package org.jk.eSked.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.jk.eSked.services.LoginService;
import org.jk.eSked.services.TimeService;
import org.jk.eSked.services.events.EventService;
import org.jk.eSked.services.groups.GroupsService;
import org.jk.eSked.services.schedule.ScheduleService;
import org.jk.eSked.services.users.UserService;
import org.jk.eSked.view.menu.MenuView;

import java.util.logging.Level;
import java.util.logging.Logger;

@Route(value = "test", layout = MenuView.class)
public class Test extends VerticalLayout {
    private final Logger LOGGER = Logger.getLogger(getClass().getName());
    public Test(LoginService loginService, ScheduleService scheduleService, EventService eventService, UserService userService, GroupsService groupsService, TimeService timeService) {
        LOGGER.log(Level.INFO, "start schedule");
        if (loginService.checkIfUserIsLogged()) {
            //VerticalLayout scheduleGrid = new ScheduleGrid(scheduleService, eventService, userService, groupsService, timeService, VaadinSession.getCurrent().getAttribute(User.class).getId());
            //add(scheduleGrid);
            LOGGER.log(Level.INFO, "stop");
        }
    }
}
