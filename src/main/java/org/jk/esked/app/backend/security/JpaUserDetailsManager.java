package org.jk.esked.app.backend.security;

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
    public UserDetails loadUserByUsername(String username) {
        try {
            return userService.getUserByUsername(username);
        } catch (UsernameNotFoundException | DataAccessException exception) {
            log.error(exception.getMessage());
        }
        return null;
    }
}
