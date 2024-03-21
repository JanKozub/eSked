package org.jk.esked.app.backend.security;

import org.jk.esked.app.backend.model.entities.User;
import org.jk.esked.app.backend.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class JpaUserDetailsManager implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(JpaUserDetailsManager.class);
    private final UserService userService;

    public JpaUserDetailsManager(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String data) {
        try {

            User user = userService.findUserByUsername(data);
            if (user == null) {
                user = userService.findUserByEmail(data);
                if (user == null)
                    throw new UsernameNotFoundException("username not found in database");
            }

            userService.changeLastLoggedInById(user.getId());
            return user;
        } catch (UsernameNotFoundException | DataAccessException exception) {
            log.error(exception.getMessage());
        }
        throw new UsernameNotFoundException("username not found in database");
    }
}
