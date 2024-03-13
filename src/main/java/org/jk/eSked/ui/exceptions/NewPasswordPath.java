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
import org.jk.eSked.backend.model.types.NotificationType;
import org.jk.eSked.backend.service.TokenService;
import org.jk.eSked.backend.service.user.MessagesService;
import org.jk.eSked.backend.service.user.UserService;
import org.jk.eSked.ui.components.myComponents.SuccessNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ValidationException;
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
        if (userId != null) {
            PasswordField newPassword = new PasswordField("Nowe Hasło");
            PasswordField confirmPassword = new PasswordField("Potwierdź hasło");

            Button changeButton = new Button("Zmień hasło!", clickEvent -> {
                try {
                    validateFields(newPassword.getValue(), confirmPassword.getValue());
                    confirmPassword.setInvalid(false);

                    userService.changePassword(userId, User.encodePassword(confirmPassword.getValue()));

                    new SuccessNotification("Twoje hasło zostało pomyślnie zmienione", NotificationType.LONG).open();

                    messagesService.addMessageForUser(new Message(
                            userId,
                            messagesService.generateMessageId(),
                            Instant.now().toEpochMilli(),
                            "Twoje hasło zostało zmienione",
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
        } else UI.getCurrent().navigate("login");

    }

    private UUID checkUrl(String url) {
        try {
            TokenValue tokenValue = tokenService.decodeToken(url);
            if (tokenValue.getUserId() != null) {
                if (tokenValue.getValue().equals("forgot")) return tokenValue.getUserId();
                else {
                    userService.changePassword(tokenValue.getUserId(), tokenValue.getValue());
                    new SuccessNotification("Twoje hasło zostało pomyślnie zmienione", NotificationType.LONG).open();
                    return null;
                }
            } else return null;
        } catch (Exception ex) {
            log.error("token decoding exception = {}", ex.getMessage());
            return null;
        }
    }

    private void validateFields(String input1, String input2) {
        if (input1.isEmpty() || input2.isEmpty()) throw new ValidationException("Oba pola muszą być wypełnione");

        if (!input1.equals(input2)) throw new ValidationException("Wpisane hasła nie są identyczne");
    }
}
