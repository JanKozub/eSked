package org.jk.esked.app.backend.security;

import com.vaadin.flow.spring.security.AuthenticationContext;
import org.jk.esked.app.backend.model.User;
import org.jk.esked.app.backend.services.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SecurityService {

    private final AuthenticationContext authenticationContext;
    private final UserService userService;

    public SecurityService(AuthenticationContext authenticationContext, UserService userService) {
        this.authenticationContext = authenticationContext;
        this.userService = userService;
    }

    public UserDetails getAuthenticatedUser() {
        return authenticationContext.getAuthenticatedUser(UserDetails.class).orElse(null);
    }

    public User getUser() {
        return userService.getUserByUsername(getAuthenticatedUser().getUsername());
    }

    public UUID getUserId() {
        return userService.getUserByUsername(getAuthenticatedUser().getUsername()).getId();
    }

    public void logout() {
        authenticationContext.logout();
    }
}