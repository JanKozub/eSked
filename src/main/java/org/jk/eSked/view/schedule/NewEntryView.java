package org.jk.eSked.view.schedule;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.juli.logging.Log;
import org.jk.eSked.component.schedule.ScheduleGridNewEntries;
import org.jk.eSked.services.TimeService;
import org.jk.eSked.services.groups.GroupsService;
import org.jk.eSked.services.schedule.ScheduleService;
import org.jk.eSked.services.users.UserService;
import org.jk.eSked.services.LoginService;
import org.jk.eSked.view.menu.MenuView;

@Route(value = "schedule/new", layout = MenuView.class)
@PageTitle("Nowy Wpis Do Planu")
public class NewEntryView extends VerticalLayout {

    public NewEntryView(LoginService loginService, ScheduleService scheduleService, GroupsService groupsService, UserService userService, TimeService timeService) {

        if (loginService.checkIfUserIsLogged()) {
            VerticalLayout scheduleGrid = new ScheduleGridNewEntries(scheduleService, groupsService, userService, timeService, 0);
            add(scheduleGrid);
        }
    }
}