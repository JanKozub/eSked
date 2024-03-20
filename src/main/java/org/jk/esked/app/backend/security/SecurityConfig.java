package org.jk.esked.app.backend.security;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.jk.esked.app.backend.services.UserService;
import org.jk.esked.app.frontend.views.LoginView;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurity {
    private static final String[] allowedGETUrlList = {
            "/favicon.ico",
            "/manifest.webmanifest",
            "/icons/**",
            "/icons/**",
            "/images/**"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> {
            for (String s : allowedGETUrlList)
                auth.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, s)).permitAll();
        });
        super.configure(http);
        setLoginView(http, LoginView.class);
    }

    @Bean
    public UserDetailsService users(UserService userService) {
        return new JpaUserDetailsManager(userService);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }
}