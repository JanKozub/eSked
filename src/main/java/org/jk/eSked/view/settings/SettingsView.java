package org.jk.eSked.view.settings;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.model.User;
import org.jk.eSked.services.LoginService;
import org.jk.eSked.services.groups.GroupsService;
import org.jk.eSked.services.schedule.ScheduleService;
import org.jk.eSked.services.users.UserService;
import org.jk.eSked.view.menu.MenuView;

@Route(value = "settings", layout = MenuView.class)
@PageTitle("Ustawienia")
public class SettingsView extends VerticalLayout {

    private User user = VaadinSession.getCurrent().getAttribute(User.class);

    public SettingsView(LoginService loginService, UserService userService, GroupsService groupsService, ScheduleService scheduleService) {
        if (loginService.checkIfUserIsLogged()) {
            setAlignItems(Alignment.CENTER);
            add(new Settingsbutton("Nazwa", user.getUsername(), "Zmień", SettingsEntryType.USERNAME, userService, groupsService, scheduleService),
                    new Settingsbutton("Email", user.getEmail(), "Zmień", SettingsEntryType.EMAIL, userService, groupsService, scheduleService),
                    new Settingsbutton("Hasło", "", "Zmień", SettingsEntryType.PASSWORD, userService, groupsService, scheduleService),
                    new Settingsbutton("Kod Grupy", Integer.toString(user.getGroupCode()), "Zmień", SettingsEntryType.SETGROUPCODE, userService, groupsService, scheduleService),
                    new Settingsbutton("Automatyczna synchronizacja z grupą", "", "", SettingsEntryType.AUTOSYNWGROUP, userService, groupsService, scheduleService),
                    new Settingsbutton("Synchronizuj Z Grupą", "", "Synchronizuj", SettingsEntryType.SYNWGROUP, userService, groupsService, scheduleService),
                    new Settingsbutton("Stwórz nową grupę", "", "Dodaj", SettingsEntryType.ADDGROUP, userService, groupsService, scheduleService),
                    new Settingsbutton("Przełącz wygląd strony ", "", "", SettingsEntryType.CHANGETHEME, userService, groupsService, scheduleService),
                    new Settingsbutton("Wyświetlanie godzin w planie lekcji", "", "", SettingsEntryType.TURNHOURS, userService, groupsService, scheduleService),
                    new Settingsbutton("Usuń konto", "", "Usuń", SettingsEntryType.DELETEACC, userService, groupsService, scheduleService)
            );
        }
    }
}