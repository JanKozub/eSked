package org.jk.eSked.view.admin.users;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.jk.eSked.component.AdminReturnButton;
import org.jk.eSked.component.SimplePopup;
import org.jk.eSked.model.User;
import org.jk.eSked.services.TimeService;
import org.jk.eSked.services.events.EventService;
import org.jk.eSked.services.groups.GroupsService;
import org.jk.eSked.services.schedule.ScheduleService;
import org.jk.eSked.services.users.UserService;
import org.jk.eSked.services.LoginService;
import org.jk.eSked.view.menu.MenuView;


@Route(value = "admin/user", layout = MenuView.class)
@PageTitle("Sprawdź Użytkownika")
class UserPageView extends VerticalLayout {

    UserPageView(LoginService loginService, ScheduleService scheduleService, UserService userService, EventService eventService, GroupsService groupsService, TimeService timeService) {

        if (loginService.checkIfUserIsLogged()) {
            if (loginService.checkIfUserIsLoggedAsAdmin()) {
                TextField textField = new TextField("Nazwa użytkownika");
                textField.setWidth("50%");

                Button button = new Button("Szukaj", event -> {
                    if (textField.getValue() == null || textField.getValue().equals("")) {
                        SimplePopup simplePopup = new SimplePopup();
                        simplePopup.open("Wprowadzona wartość jest niepoprawna");
                        textField.clear();
                    } else {
                        User user = findUser(userService, textField.getValue());
                        if (user != null) {
                            removeAll();
                            UserFoundView userFoundView = new UserFoundView(loginService, scheduleService, eventService, userService, groupsService, timeService, user);
                            add(userFoundView);
                        }
                    }
                });
                button.setWidth("50%");

                setAlignItems(Alignment.CENTER);
                add(new AdminReturnButton(), textField, button);
            }
        }
    }

    private User findUser(UserService userService, String username) {
        for (User user : userService.getUsers()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        SimplePopup simplePopup = new SimplePopup();
        simplePopup.open("Nie znaleziono użytkownika");
        return null;
    }
}