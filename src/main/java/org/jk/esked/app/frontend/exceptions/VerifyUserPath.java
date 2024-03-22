package org.jk.esked.app.frontend.exceptions;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.jk.esked.app.backend.model.TokenValue;
import org.jk.esked.app.backend.model.exceptions.ValidationException;
import org.jk.esked.app.backend.model.types.NotificationType;
import org.jk.esked.app.backend.services.MessageService;
import org.jk.esked.app.backend.services.UserService;
import org.jk.esked.app.backend.services.utilities.TokenService;
import org.jk.esked.app.frontend.components.other.SuccessNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AnonymousAllowed
@Route(value = "verify")
public class VerifyUserPath extends VerticalLayout implements HasUrlParameter<String> {
    private static final Logger log = LoggerFactory.getLogger(VerifyUserPath.class);
    private final UserService userService;
    private final TokenService tokenService;
    private final MessageService messagesService;

    public VerifyUserPath(UserService userService, TokenService tokenService, MessageService messageService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.messagesService = messageService;
    }

    @Override
    public void setParameter(BeforeEvent event, String url) {
        try {
            TokenValue tokenValue = tokenService.decodeToken(url);
            if (tokenValue.getUserId() == null) throw new ValidationException("token not found(null)");

            userService.changeVerifiedById(tokenValue.getUserId(), true);
            messagesService.saveMessage(userService.findById(tokenValue.getUserId()), getTranslation("notification.account.activated"));
        } catch (Exception ex) {
            log.error("token decoding exception = {}", ex.getMessage());
            UI.getCurrent().navigate("login");
            return;
        }

        UI.getCurrent().navigate("login");
        new SuccessNotification(getTranslation("notification.account.activated"), NotificationType.LONG).open();
    }
}
