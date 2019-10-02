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

@Route(value = "email")
public class NewEmailPath extends VerticalLayout implements HasUrlParameter<String> {
    private static final Logger log = LoggerFactory.getLogger(NewPasswordPath.class);
    private UserService userService;
    private TokenService tokenService;

    public NewEmailPath(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @Override
    public void setParameter(BeforeEvent event, String url) {
        TokenValue tokenValue = checkUrl(url);
        if (tokenValue != null) {

            userService.changeEmail(tokenValue.getUserId(), tokenValue.getValue());

            new SuccessNotification("Twój email został pomyślnie zmieniony", NotificationType.LONG).open();
            UI.getCurrent().navigate("settings");

        } else UI.getCurrent().navigate("settings");
    }

    private TokenValue checkUrl(String url) {
        try {
            TokenValue tokenValue = tokenService.decodeToken(url);
            if (tokenValue.getUserId() != null && tokenValue.getValue().contains("@")) {
                return tokenValue;
            } else return null;
        } catch (Exception ex) {
            log.error("token decoding exception = {}", ex.getMessage());
            return null;
        }
    }
}