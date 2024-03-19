package org.jk.esked.app.frontend.components.admin;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import org.jk.esked.app.backend.model.User;
import org.jk.esked.app.backend.model.types.NotificationType;
import org.jk.esked.app.backend.services.EncryptionService;
import org.jk.esked.app.backend.services.UserService;
import org.jk.esked.app.frontend.components.Line;
import org.jk.esked.app.frontend.components.SuccessNotification;

public class UserCreator extends VerticalLayout {
    private final EncryptionService encryptionService;

    public UserCreator(UserService userService, EncryptionService encryptionService) {
        this.encryptionService = encryptionService;

        Line line = new Line();
        Text text = new Text(getTranslation("title"));
        TextField username = new TextField(getTranslation("username"));
        TextField email = new TextField("email");
        PasswordField password = new PasswordField(getTranslation("password"));

        Button addUser = new Button(getTranslation("add"), e -> {
            boolean canBeCreated = !userService.getAllUserUsernames().contains(username.getValue()) && !userService.getAllUserEmails().contains(email.getValue());

            if (canBeCreated) createUser(userService, username.getValue(), email.getValue(), password.getValue());
            else denyUserCreation();
        });

        add(line, text, username, email, password, addUser);
        setAlignItems(Alignment.CENTER);
    }

    private void createUser(UserService userService, String username, String email, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(encryptionService.encodePassword(password));
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
