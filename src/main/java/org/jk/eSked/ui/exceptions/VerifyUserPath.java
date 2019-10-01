package org.jk.eSked.ui.exceptions;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import org.jk.eSked.backend.model.TokenValue;
import org.jk.eSked.backend.model.types.NotificationType;
import org.jk.eSked.backend.service.TokenService;
import org.jk.eSked.backend.service.UserService;
import org.jk.eSked.ui.components.myImpl.SuccessNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Route(value = "verify")
public class VerifyUserPath extends VerticalLayout implements HasUrlParameter<String> {
    private static final Logger log = LoggerFactory.getLogger(VerifyUserPath.class);
    private UserService userService;
    private TokenService tokenService;

    public VerifyUserPath(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @Override
    public void setParameter(BeforeEvent event, String url) {
        try {
            TokenValue tokenValue = tokenService.decodeToken(url);
            if (tokenValue.getUserId() != null) {
                userService.setVerified(tokenValue.getUserId(), true);
                new SuccessNotification("Twoje konto zostało aktywowane", NotificationType.LONG).open();
            }
        } catch (Exception ex) {
            log.error("token decoding exception = {}", ex.getMessage());
        }
        UI.getCurrent().navigate("login");
    }
}