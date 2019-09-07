package org.jk.eSked.view.admin.users;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.jk.eSked.components.myImpl.AdminReturnButton;
import org.jk.eSked.model.User;
import org.jk.eSked.services.LoginService;
import org.jk.eSked.services.emailService.EmailService;
import org.jk.eSked.services.events.EventService;
import org.jk.eSked.services.hours.HoursService;
import org.jk.eSked.services.schedule.ScheduleService;
import org.jk.eSked.services.users.UserService;
import org.jk.eSked.view.MenuView;


@Route(value = "admin/user", layout = MenuView.class)
@PageTitle("Sprawdź Użytkownika")
class UserPageView extends VerticalLayout {

    UserPageView(LoginService loginService, ScheduleService scheduleService, UserService userService, EventService eventService, HoursService hoursService, EmailService emailService) {

        if (loginService.checkIfUserIsLogged()) {
            if (loginService.checkIfUserIsLoggedAsAdmin()) {
                TextField textField = new TextField("Nazwa użytkownika");
                textField.setWidth("50%");

                Button button = new Button("Szukaj", event -> {
                    if (textField.getValue().equals("")) {
                        textField.setErrorMessage("Pole nie może być puste");
                        textField.setInvalid(true);
                        textField.clear();
                    } else {
                        textField.setInvalid(false);
                        User user = findUser(userService, textField.getValue());
                        if (user != null) {
                            textField.setInvalid(false);
                            removeAll();
                            UserFoundView userFoundView = new UserFoundView(loginService, scheduleService, eventService, userService, hoursService, emailService, user);
                            add(userFoundView);
                        } else {
                            textField.setErrorMessage("Użytkownik nie istnieje");
                            textField.setInvalid(true);
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
        return null;
    }
}