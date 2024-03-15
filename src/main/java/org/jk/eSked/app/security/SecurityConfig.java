package org.jk.eSked.app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .requestCache(c -> new CustomRequestCache())
                .authorizeHttpRequests().requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
                .anyRequest().authenticated()
                .and().formLogin().loginPage("/login").permitAll()
                .and().logout().logoutSuccessUrl("/login");

        return http.build();
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
