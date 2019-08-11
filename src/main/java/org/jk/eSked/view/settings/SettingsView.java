package org.jk.eSked.view.settings;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.jk.eSked.services.GroupSynService;
import org.jk.eSked.services.events.EventService;
import org.jk.eSked.services.groups.GroupsService;
import org.jk.eSked.services.schedule.ScheduleService;
import org.jk.eSked.services.users.UserService;
import org.jk.eSked.services.LoginService;
import org.jk.eSked.view.menu.MenuView;

@Route(value = "settings", layout = MenuView.class)
@PageTitle("Ustawienia")
public class SettingsView extends VerticalLayout {

    public SettingsView(LoginService loginService, UserService userService, ScheduleService scheduleService, GroupsService groupsService, EventService eventService, GroupSynService groupSynService) {

        if (loginService.checkIfUserIsLogged()) {
            setAlignItems(Alignment.CENTER);
            add(new SettingsEntry(userService, scheduleService,
                            groupsService, groupSynService, SettingsEntryType.USERNAME),
                    new SettingsEntry(userService, scheduleService,
                            groupsService, groupSynService, SettingsEntryType.PASSWORD),
                    new SettingsEntry(userService, scheduleService,
                            groupsService, groupSynService, SettingsEntryType.EMAIL),
                    new SettingsEntry(userService, scheduleService,
                            groupsService, groupSynService, SettingsEntryType.SETGROUPCODE),
                    new SettingsEntry(userService, scheduleService,
                            groupsService, groupSynService, SettingsEntryType.SYNWGROUP),
                    new SettingsEntry(userService, scheduleService,
                            groupsService, groupSynService, SettingsEntryType.AUTOSYNWGROUP),
                    new SettingsEntry(userService, scheduleService,
                            groupsService, groupSynService, SettingsEntryType.ADDGROUP),
                    new SettingsEntry(userService, scheduleService,
                            groupsService, groupSynService, SettingsEntryType.CHANGETHEME),
                    new SettingsEntry(userService, scheduleService,
                            groupsService, groupSynService, SettingsEntryType.TURNHOURS),
                    new SettingsEntry(userService, scheduleService,
                            groupsService, groupSynService, SettingsEntryType.DELETEACC));
        }
    }
}