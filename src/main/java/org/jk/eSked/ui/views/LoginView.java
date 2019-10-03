package org.jk.eSked.ui.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.backend.model.User;
import org.jk.eSked.backend.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

@Route(value = "login")
@PageTitle("Login")
public class LoginView extends VerticalLayout {

    private LoginOverlay loginOverlay = new LoginOverlay();

    @Autowired
    public LoginView(AuthenticationManager authenticationManager, UserService userService) {
        H1 title = new H1();
        title.getStyle().set("color", "var(--lumo-base-color)");
        Icon icon = VaadinIcon.CALENDAR.create();
        icon.setSize("30px");
        icon.getStyle().set("top", "-4px");
        title.add(icon);
        title.add(new Text(" eSked"));
        loginOverlay.setTitle(title);
        loginOverlay.addLoginListener(form -> {
            try {
                final Authentication authentication = authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(form.getUsername(), User.encodePassword(form.getPassword())));
                if (authentication != null) {
                    loginOverlay.close();
                    VaadinSession.getCurrent().setAttribute(User.class, userService.getUserByUsername(form.getUsername()));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    UI.getCurrent().navigate("schedule");
                }
            } catch (AuthenticationException ex) {
                loginOverlay.setError(true);
            }
        });
        loginOverlay.setOpened(true);
        loginOverlay.setI18n(createPolishI18n());
        add(loginOverlay);
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