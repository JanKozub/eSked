package org.jk.eSked.app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .requestCache(c -> new CustomRequestCache())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll();
                    auth.anyRequest().authenticated();
                })
                .formLogin(l -> l.loginPage("/login").permitAll())
                .logout(l -> l.logoutSuccessUrl("/login"))
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    CustomRequestCache requestCache() {
        return new CustomRequestCache();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsManager();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/VAADIN/**",
                "/favicon.ico",
                "/manifest.webmanifest",
                "/sw.js",
                "/icons/**",
                "/images/**");
    }
}
