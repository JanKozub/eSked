package org.jk.esked.app.frontend.exceptions;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.jk.esked.app.backend.model.TokenValue;
import org.jk.esked.app.backend.model.entities.Message;
import org.jk.esked.app.backend.model.exceptions.ValidationException;
import org.jk.esked.app.backend.model.types.NotificationType;
import org.jk.esked.app.backend.security.SecurityService;
import org.jk.esked.app.backend.services.MessageService;
import org.jk.esked.app.backend.services.UserService;
import org.jk.esked.app.backend.services.utilities.TokenService;
import org.jk.esked.app.frontend.components.other.SuccessNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@AnonymousAllowed
@Route(value = "password")
public class NewPasswordPath extends VerticalLayout implements HasUrlParameter<String> {
    private static final Logger log = LoggerFactory.getLogger(NewPasswordPath.class);
    private final UserService userService;
    private final TokenService tokenService;
    private final MessageService messageService;
    private final SecurityService securityService;

    public NewPasswordPath(UserService userService, SecurityService securityService, TokenService tokenService, MessageService messageService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.messageService = messageService;
        this.securityService = securityService;
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

                userService.changePasswordByUserId(userId, securityService.encodePassword(confirmPassword.getValue()));

                new SuccessNotification(getTranslation("notification.password.changed"), NotificationType.LONG).open();

                Message message = new Message();
                message.setUser(userService.getUserById(userId));
                message.setText(getTranslation("notification.password.changed"));
                messageService.saveMessage(message);

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

            userService.changePasswordByUserId(tokenValue.getUserId(), tokenValue.getValue());
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
