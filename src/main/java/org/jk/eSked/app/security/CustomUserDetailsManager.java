package org.jk.eSked.app.security;

import org.jk.eSked.backend.configuration.ApplicationContextHolder;
import org.jk.eSked.backend.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

public class CustomUserDetailsManager implements UserDetailsManager {
    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsManager.class);
    private final Map<String, UserDetails> users = new HashMap<>();

    private UserService userService;

    CustomUserDetailsManager() {
        userService = ApplicationContextHolder.getContext().getBean(UserService.class);
        log.info("Loaded {} users to Detail manager", userService.getUserDetails().size());
        for (UserDetails userDetails : userService.getUserDetails()) {
            this.createUser(userDetails);
        }
    }

    public void createUser(UserDetails user) {
        Assert.isTrue(!this.userExists(user.getUsername()), "user should not exist");
        this.users.put(user.getUsername().toLowerCase(), user);
    }

    public void deleteUser(String username) {
        this.users.remove(username.toLowerCase());
    }

    public void updateUser(UserDetails user) {
        Assert.isTrue(this.userExists(user.getUsername()), "user should exist");
        this.users.put(user.getUsername().toLowerCase(), user);
    }

    public boolean userExists(String username) {
        return this.users.containsKey(username.toLowerCase());
    }

    public void changePassword(String oldPassword, String newPassword) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser == null) {
            throw new AccessDeniedException("Cant change password when user equals null");
        } else {
            String username = currentUser.getName();
            userService.changePassword(userService.getIdByUsername(username), newPassword);
        }
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = this.users.get(username.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException(username);
        } else {
            return new User(user.getUsername(), user.getPassword(), user.isEnabled(), user.isAccountNonExpired(), user.isCredentialsNonExpired(), user.isAccountNonLocked(), user.getAuthorities());
        }
    }
}