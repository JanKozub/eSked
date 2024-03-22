package org.jk.esked.app.frontend.exceptions;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.jk.esked.app.backend.model.TokenValue;
import org.jk.esked.app.backend.model.types.NotificationType;
import org.jk.esked.app.backend.security.SecurityService;
import org.jk.esked.app.backend.services.MessageService;
import org.jk.esked.app.backend.services.UserService;
import org.jk.esked.app.backend.services.utilities.TokenService;
import org.jk.esked.app.frontend.components.other.SuccessNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AnonymousAllowed
@Route(value = "email")
public class NewEmailPath extends VerticalLayout implements HasUrlParameter<String> {
    private static final Logger log = LoggerFactory.getLogger(NewEmailPath.class);
    private final UserService userService;
    private final TokenService tokenService;
    private final MessageService messageService;
    private final SecurityService securityService;

    public NewEmailPath(SecurityService securityService, UserService userService, TokenService tokenService, MessageService messageService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.messageService = messageService;
        this.securityService = securityService;
    }

    @Override
    public void setParameter(BeforeEvent event, String url) {
        TokenValue tokenValue = checkUrl(url);
        if (tokenValue == null) {
            UI.getCurrent().navigate("/schedule");
            return;
        }
        userService.changeEmailById(tokenValue.getUserId(), tokenValue.getValue());

        new SuccessNotification(getTranslation("notification.email.successful.change"), NotificationType.LONG).open();
        UI.getCurrent().navigate("/schedule");
        messageService.saveMessage(securityService.getUser(), getTranslation("notification.email.changed.to") + " \"" + tokenValue.getValue() + "\"");
    }

    private TokenValue checkUrl(String url) {
        try {
            TokenValue tokenValue = tokenService.decodeToken(url);
            if (tokenValue.getUserId() == null || !tokenValue.getValue().contains("@")) return null;
            return tokenValue;
        } catch (Exception ex) {
            log.error("token decoding exception = {}", ex.getMessage());
            return null;
        }
    }
}
