package org.jk.esked.app.frontend.components.admin;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import org.jk.esked.app.backend.model.types.NotificationType;
import org.jk.esked.app.backend.security.SecurityService;
import org.jk.esked.app.backend.services.UserService;
import org.jk.esked.app.frontend.components.notifications.ErrorNotification;
import org.jk.esked.app.frontend.components.notifications.SuccessNotification;
import org.jk.esked.app.frontend.components.other.HorizontalLine;

public class UserCreator extends VerticalLayout {
    private final SecurityService securityService;

    public UserCreator(UserService userService, SecurityService securityService) {
        this.securityService = securityService;

        Text text = new Text(getTranslation("user.create"));
        TextField username = new TextField(getTranslation("field.username.title"));
        EmailField email = new EmailField(getTranslation("field.email.title"));
        PasswordField password = new PasswordField(getTranslation("field.password.title"));

        Button addUser = new Button(getTranslation("add"), e -> {
            if (!userService.isUsernameRegistered(username.getValue()) && !userService.isEmailRegistered(email.getValue())) {
                createUser(userService, username.getValue(), email.getValue(), password.getValue());
                username.clear();
                email.clear();
                password.clear();
                return;
            }

            new ErrorNotification(getTranslation("added.not"), NotificationType.SHORT.getDuration()).open();
        });

        addClassName("user-creator");
        add(new HorizontalLine(), text, username, email, password, addUser);
    }

    private void createUser(UserService userService, String username, String email, String password) {
        userService.saveUser(username, securityService.encodePassword(password), email);

        SuccessNotification successNotification = new SuccessNotification(getTranslation("added"), NotificationType.SHORT);
        successNotification.open();
    }
}
