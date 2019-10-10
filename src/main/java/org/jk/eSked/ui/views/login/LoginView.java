package org.jk.eSked.ui.views.login;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.app.security.SecurityUtils;
import org.jk.eSked.backend.model.User;
import org.jk.eSked.backend.service.EmailService;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.backend.service.TimeService;
import org.jk.eSked.backend.service.user.GroupService;
import org.jk.eSked.backend.service.user.UserService;
import org.jk.eSked.ui.components.login.LoginExceptionDialog;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

@Route(value = "login")
@PageTitle("Login")
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
@PWA(name = "eSked", shortName = "eSked", startPath = "login", description = "schedule app")
public class LoginView extends VerticalLayout {
    private UserService userService;
    private GroupService groupService;

    public LoginView(AuthenticationManager authenticationManager, UserService userService, GroupService groupService, EmailService emailService) {
        this.userService = userService;
        this.groupService = groupService;

        H1 title = new H1();
        title.getStyle().set("color", "var(--lumo-base-color)");
        Icon icon = VaadinIcon.CALENDAR.create();
        icon.setSize("30px");
        icon.getStyle().set("top", "-4px");
        title.add(icon);
        title.add(new Text(" eSked"));
        LoginOverlay loginOverlay = new LoginOverlay();
        loginOverlay.setTitle(title);
        loginOverlay.addLoginListener(form -> {
            try {
                String username = form.getUsername();
                if (username.contains("@")) username = userService.getUsernameByEmail(username);

                final Authentication authentication = authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(username, User.encodePassword(form.getPassword())));
                if (authentication != null) {
                    loginOverlay.close();
                    VaadinSession.getCurrent().setAttribute(User.class, userService.getUserByUsername(username));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    afterAuth(userService.getUser(SessionService.getUserId()));
                }
            } catch (AuthenticationException ex) {
                loginOverlay.setError(true);
            }
        });
        loginOverlay.addForgotPasswordListener(click -> new LoginExceptionDialog(userService, emailService).open());
        loginOverlay.setOpened(true);
        loginOverlay.setI18n(createPolishI18n());
        add(loginOverlay);

        if (SecurityUtils.isUserLoggedIn()) {
            UI.getCurrent().navigate("schedule");
            UI.getCurrent().getPage().reload();
        }
    }

    private void afterAuth(User user) {
        SessionService.setAutoTheme();

        userService.setLastLogged(user.getId(), TimeService.now());

        if (groupService.getGroupNames().stream().noneMatch(s -> s.equals(groupService.getGroupName(user.getGroupCode()))))
            userService.setGroupCode(user.getId(), 0);

        if (userService.getGroupCode(user.getId()) != 0)
            groupService.synchronizeWGroup(user.getId(), user.getGroupCode());

        UI.getCurrent().navigate("schedule");
    }

    private LoginI18n createPolishI18n() {
        final LoginI18n i18n = LoginI18n.createDefault();

        i18n.setHeader(new LoginI18n.Header());
        i18n.getForm().setTitle("Zaloguj się");
        i18n.getForm().setUsername("Nazwa użytkownika lub email");
        i18n.getForm().setPassword("Hasło");
        i18n.getForm().setSubmit("Zaloguj się");
        i18n.getForm().setForgotPassword("Stwórz konto lub odzyskaj hasło");
        i18n.getErrorMessage().setTitle("Nieprawidłowe dane");
        i18n.getErrorMessage().setMessage("Sprawdź czy wprowadzone dane są prawidłowe i spróbuj ponownie.");
        i18n.setAdditionalInformation("Kontakt z admininstratorem pod adresem: eskedinfo@gmail.com");
        return i18n;
    }
}
