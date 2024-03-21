package org.jk.esked.app.frontend.components.admin;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import org.jk.esked.app.backend.model.entities.User;
import org.jk.esked.app.backend.model.types.NotificationType;
import org.jk.esked.app.backend.security.SecurityService;
import org.jk.esked.app.backend.services.UserService;
import org.jk.esked.app.frontend.components.other.HorizontalLine;
import org.jk.esked.app.frontend.components.other.SuccessNotification;

public class UserCreator extends VerticalLayout {
    private final SecurityService securityService;

    public UserCreator(UserService userService, SecurityService securityService) {
        this.securityService = securityService;

        HorizontalLine horizontalLine = new HorizontalLine();
        Text text = new Text(getTranslation("title"));
        TextField username = new TextField(getTranslation("username"));
        TextField email = new TextField("email");
        PasswordField password = new PasswordField(getTranslation("password"));

        Button addUser = new Button(getTranslation("add"), e -> {
            boolean canBeCreated = !userService.findAllUsernames().contains(username.getValue()) && !userService.findAllRegisteredEmails().contains(email.getValue());

            if (canBeCreated) createUser(userService, username.getValue(), email.getValue(), password.getValue());
            else denyUserCreation();
        });

        add(horizontalLine, text, username, email, password, addUser);
        setAlignItems(Alignment.CENTER);
    }

    private void createUser(UserService userService, String username, String email, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(securityService.encodePassword(password));
        user.setEmail(email);
        userService.saveUser(user);

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
