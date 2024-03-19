package org.jk.esked.app.frontend.views.admin.users;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.jk.esked.app.backend.model.User;
import org.jk.esked.app.backend.model.exceptions.ValidationException;
import org.jk.esked.app.backend.services.*;
import org.jk.esked.app.frontend.components.AdminReturnButton;
import org.jk.esked.app.frontend.components.admin.UserCreator;
import org.jk.esked.app.frontend.components.admin.UserLayout;
import org.jk.esked.app.frontend.views.MainLayout;

@Route(value = "admin/user", layout = MainLayout.class)
@RolesAllowed("ADMIN")
class FindUserView extends VerticalLayout implements HasDynamicTitle {
    FindUserView(ScheduleService scheduleService, UserService userService, EventService eventService, HourService hourService, EmailService emailService, GroupService groupService, MessageService messageService, EncryptionService encryptionService) {

        TextField textField = new TextField(getTranslation("username"));
        textField.setWidth("50%");
        textField.focus();

        Button searchForUserButton = new Button(getTranslation("search"), event -> {
            try {
                User user = validateInput(userService, textField.getValue());
                textField.setInvalid(false);
                removeAll();

                add(new UserLayout(user, userService, emailService, groupService, scheduleService, eventService, hourService, messageService));
                setAlignItems(Alignment.CENTER);
            } catch (ValidationException ex) {
                textField.setErrorMessage(ex.getMessage());
                textField.setInvalid(true);
            }
        });
        searchForUserButton.addClickShortcut(Key.ENTER);
        searchForUserButton.setWidth("50%");

        setAlignItems(Alignment.CENTER);
        add(new AdminReturnButton(), textField, searchForUserButton, new UserCreator(userService, encryptionService));
    }

    private User validateInput(UserService userService, String input) throws ValidationException{
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
