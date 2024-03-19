package org.jk.esked.app.backend.security;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.jk.esked.app.backend.model.types.UserType;
import org.jk.esked.app.backend.services.UserService;
import org.jk.esked.app.frontend.views.LoginView;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurity {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> { //TODO as list
            auth.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/VAADIN/**")).permitAll();
            auth.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/favicon.ico")).permitAll();
            auth.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/manifest.webmanifest")).permitAll();
            auth.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/sw.js")).permitAll();
            auth.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/icons/**")).permitAll();
            auth.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/images/**")).permitAll();
        });
        super.configure(http);
        setLoginView(http, LoginView.class);
    }

    @Bean
    public UserDetailsService users(UserService userService) {
        List<org.jk.esked.app.backend.model.User> users = userService.getAllUsers();

        InMemoryUserDetailsManager detailsManager = new InMemoryUserDetailsManager();
        users.forEach(u -> {
            String[] roles = {"USER"};
            if (u.getUsername().equals("admin") || u.getUserType() == UserType.ADMIN)
                roles = new String[]{"USER", "ADMIN"};

            UserDetails user = User.builder() //TODO provide bcrypt key?
                    .username(u.getUsername())
                    .password("{bcrypt}" + u.getPassword())
                    .roles(roles)
                    .build();

            detailsManager.createUser(user);
        });
        return detailsManager;
    }
}