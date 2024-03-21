package org.jk.esked.app.backend.security;

import com.vaadin.flow.spring.security.AuthenticationContext;
import org.jk.esked.app.backend.model.entities.User;
import org.jk.esked.app.backend.services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@PropertySource("/passwords.properties")
public class SecurityService {
    private final String bCryptKey;
    private final AuthenticationContext authenticationContext;
    private final UserService userService;

    public SecurityService(AuthenticationContext authenticationContext, UserService userService, @Value("${json.key2}") String bCryptKey) {
        this.authenticationContext = authenticationContext;
        this.userService = userService;
        this.bCryptKey = bCryptKey;
    }

    public UserDetails getAuthenticatedUser() {
        return authenticationContext.getAuthenticatedUser(UserDetails.class).orElse(null);
    }

    public User getUser() {
        return userService.findUserByUsername(getAuthenticatedUser().getUsername());
    }

    public UUID getUserId() {
        return userService.findUserByUsername(getAuthenticatedUser().getUsername()).getId();
    }

    public void logout() {
        authenticationContext.logout();
    }

    public String encodePassword(String password) {
        return BCrypt.hashpw(password, bCryptKey);
    }
}