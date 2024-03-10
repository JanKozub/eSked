package org.jk.eSked.ui.components.login;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.backend.model.User;
import org.jk.eSked.backend.service.EmailService;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.backend.service.TimeService;
import org.jk.eSked.backend.service.user.GroupService;
import org.jk.eSked.backend.service.user.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Locale;

public class MyLoginOverlay extends LoginOverlay {
    public MyLoginOverlay(AuthenticationManager authenticationManager, UserService userService, GroupService groupService, EmailService emailService) {
        setTitle(createTitle());
        setDescription("");
        addLoginListener(form -> {
            try {
                String username = form.getUsername();
                if (username.contains("@")) username = userService.getUsernameByEmail(username);

                final Authentication authentication = authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(username, User.encodePassword(form.getPassword())));
                if (authentication != null) {
                    close();
                    try {
                        VaadinSession.getCurrent().getLockInstance().lock();
                        VaadinSession.getCurrent().setAttribute(User.class, userService.getUserByUsername(username));
                    } finally {
                        VaadinSession.getCurrent().getLockInstance().unlock();
                    }
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    afterAuth(userService.getUser(SessionService.getUserId()), userService, groupService);
                }
            } catch (AuthenticationException ex) {
                setError(true);
            }
        });
        addForgotPasswordListener(click -> new LoginExceptionDialog(userService, emailService).open());
        setOpened(true);
        setI18n(createI18n());
    }

    private H1 createTitle() {
        H1 title = new H1();
        title.getStyle().set("color", "var(--lumo-base-color)");
        Icon icon = VaadinIcon.CALENDAR.create();
        icon.setSize("30px");
        icon.getStyle().set("top", "-4px");
        title.add(icon);
        title.add(new Text(" eSked"));

        return title;
    }

    private void afterAuth(User user, UserService userService, GroupService groupService) {
        SessionService.setAutoTheme();

        userService.setLastLogged(user.getId(), TimeService.now());

        if (groupService.getGroupNames().stream().noneMatch(s -> s.equals(groupService.getGroupName(user.getGroupCode()))))
            userService.setGroupCode(user.getId(), 0);

        if (userService.getGroupCode(user.getId()) != 0)
            groupService.synchronizeWGroup(user.getId(), user.getGroupCode());

        UI.getCurrent().navigate("schedule");
    }

    private LoginI18n createI18n() {
        final LoginI18n i18n = LoginI18n.createDefault();
        LoginI18n.Form form = i18n.getForm();
        Locale locale = VaadinSession.getCurrent().getLocale();

        form.setTitle(getTranslation(locale, "login_title"));
        form.setUsername(getTranslation(locale, "login_username"));
        form.setPassword(getTranslation(locale, "password"));
        form.setSubmit(getTranslation(locale, "login_submit"));
        form.setForgotPassword(getTranslation(locale, "login_forgot_password"));
        i18n.getErrorMessage().setTitle(getTranslation(locale, "login_error_title"));
        i18n.getErrorMessage().setMessage(getTranslation(locale, "login_error_message"));
        i18n.setAdditionalInformation(getTranslation(locale, "login_information"));

        return i18n;
    }
}
