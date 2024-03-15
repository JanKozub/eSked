package org.jk.eSked.ui.exceptions;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import org.jk.eSked.backend.model.Message;
import org.jk.eSked.backend.model.TokenValue;
import org.jk.eSked.backend.model.User;
import org.jk.eSked.backend.model.exceptions.ValidationException;
import org.jk.eSked.backend.model.types.NotificationType;
import org.jk.eSked.backend.service.TokenService;
import org.jk.eSked.backend.service.user.MessagesService;
import org.jk.eSked.backend.service.user.UserService;
import org.jk.eSked.ui.components.myComponents.SuccessNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.UUID;

@Route(value = "password")
public class NewPasswordPath extends VerticalLayout implements HasUrlParameter<String> {
    private static final Logger log = LoggerFactory.getLogger(NewPasswordPath.class);
    private final UserService userService;
    private final TokenService tokenService;
    private final MessagesService messagesService;

    public NewPasswordPath(UserService userService, TokenService tokenService, MessagesService messagesService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.messagesService = messagesService;
    }

    @Override
    public void setParameter(BeforeEvent event, String url) {
        UUID userId = checkUrl(url);
        if (userId == null) {
            UI.getCurrent().navigate("login");
            return;
        }

        PasswordField newPassword = new PasswordField(getTranslation("field.password.placeholder"));
        PasswordField confirmPassword = new PasswordField(getTranslation("field.password.confirm"));

        Button changeButton = new Button(getTranslation("field.password.change") + "!", clickEvent -> {
            try {
                validateFields(newPassword.getValue(), confirmPassword.getValue());
                confirmPassword.setInvalid(false);

                userService.changePassword(userId, User.encodePassword(confirmPassword.getValue()));

                new SuccessNotification(getTranslation("notification.password.changed"), NotificationType.LONG).open();
                messagesService.addMessageForUser(new Message(
                        userId,
                        messagesService.generateMessageId(),
                        Instant.now().toEpochMilli(),
                        getTranslation("notification.password.changed"),
                        false
                ));

                UI.getCurrent().navigate("login");
            } catch (ValidationException ex) {
                confirmPassword.setErrorMessage(ex.getMessage());
                confirmPassword.setInvalid(true);
            }
        });
        changeButton.addClickShortcut(Key.ENTER);

        setAlignItems(Alignment.CENTER);
        add(newPassword, confirmPassword, changeButton);
    }

    private UUID checkUrl(String url) {
        try {
            TokenValue tokenValue = tokenService.decodeToken(url);
            if (tokenValue.getUserId() == null) return null;
            if (tokenValue.getValue().equals("forgot")) return tokenValue.getUserId();

            userService.changePassword(tokenValue.getUserId(), tokenValue.getValue());
            new SuccessNotification(getTranslation("notification.password.changed"), NotificationType.LONG).open();
        } catch (Exception ex) {
            log.error("token decoding exception = {}", ex.getMessage());
        }
        return null;
    }

    private void validateFields(String input1, String input2) throws ValidationException {
        if (input1.isEmpty() || input2.isEmpty())
            throw new ValidationException(getTranslation("exception.fields.cannot.be.empty"));

        if (!input1.equals(input2)) throw new ValidationException(getTranslation("exception.password.not.match"));
    }
}
