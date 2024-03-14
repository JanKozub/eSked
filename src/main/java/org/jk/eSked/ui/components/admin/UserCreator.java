package org.jk.eSked.ui.components.admin;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import org.jk.eSked.backend.model.User;
import org.jk.eSked.backend.model.types.NotificationType;
import org.jk.eSked.backend.service.TimeService;
import org.jk.eSked.backend.service.user.UserService;
import org.jk.eSked.ui.components.myComponents.Line;
import org.jk.eSked.ui.components.myComponents.SuccessNotification;

import java.util.UUID;

public class UserCreator extends VerticalLayout {
    public UserCreator(UserService userService) {
        Line line = new Line();
        Text text = new Text(getTranslation("title"));
        TextField username = new TextField(getTranslation("username"));
        TextField email = new TextField("email");
        PasswordField password = new PasswordField(getTranslation("password"));

        Button addUser = new Button(getTranslation("add"), e -> {
            boolean canBeCreated = !userService.getUsernames().contains(username.getValue()) && !userService.getEmails().contains(email.getValue());

            if (canBeCreated) createUser(userService, username.getValue(), email.getValue(), password.getValue());
            else denyUserCreation();
        });

        add(line, text, username, email, password, addUser);
        setAlignItems(Alignment.CENTER);
    }

    private void createUser(UserService userService, String username, String email, String password) {
        userService.addUser(new User(UUID.randomUUID(), username, User.encodePassword(password),
                false, false, email, 0, false,
                false, TimeService.now(), TimeService.now(), false));
        SuccessNotification successNotification = new SuccessNotification(getTranslation("added"), NotificationType.SHORT);
        successNotification.open();
    }

    private void denyUserCreation() {
        Notification notification = new Notification(getTranslation("added.not"), NotificationType.SHORT.getDuration());
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.setPosition(Notification.Position.TOP_END);
        notification.open();
    }
}
