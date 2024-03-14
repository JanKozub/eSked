package org.jk.eSked.ui.views.admin.users;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import org.jk.eSked.backend.model.User;
import org.jk.eSked.backend.service.EmailService;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.backend.service.user.*;
import org.jk.eSked.ui.components.admin.UserCreator;
import org.jk.eSked.ui.components.admin.UserLayout;
import org.jk.eSked.ui.components.myComponents.AdminReturnButton;
import org.jk.eSked.ui.views.MainLayout;
import org.springframework.security.access.annotation.Secured;

import javax.validation.ValidationException;

@Route(value = "admin/user", layout = MainLayout.class)
@Secured("ROLE_ADMIN")
class FindUserView extends VerticalLayout implements HasDynamicTitle {
    FindUserView(ScheduleService scheduleService, UserService userService, EventService eventService, HoursService hoursService, EmailService emailService, GroupService groupService) {
        SessionService.setAutoTheme();

        TextField textField = new TextField(getTranslation("username"));
        textField.setWidth("50%");
        textField.focus();

        Button searchForUserButton = new Button(getTranslation("search"), event -> {
            try {
                User user = validateInput(userService, textField.getValue());
                textField.setInvalid(false);
                removeAll();

                add(new UserLayout(user, userService, emailService, groupService, scheduleService, eventService, hoursService));
                setAlignItems(Alignment.CENTER);
            } catch (ValidationException ex) {
                textField.setErrorMessage(ex.getMessage());
                textField.setInvalid(true);
            }
        });
        searchForUserButton.addClickShortcut(Key.ENTER);
        searchForUserButton.setWidth("50%");

        setAlignItems(Alignment.CENTER);
        add(new AdminReturnButton(), textField, searchForUserButton, new UserCreator(userService));
    }

    private User validateInput(UserService userService, String input) {
        if (input.isEmpty()) throw new ValidationException(getTranslation("exception.empty.field"));

        User user = userService.getUserByUsername(input);
        if (user == null) throw new ValidationException(getTranslation("exception.user.not.exists"));

        return user;
    }

    @Override
    public String getPageTitle() {
        return getTranslation("page.check.user");
    }
}
