package org.jk.eSked.view;

import com.vaadin.flow.component.AttachEvent;
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
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.components.CheckTimeTheme;
import org.jk.eSked.components.dialogs.ProblemDialog;
import org.jk.eSked.model.Notification;
import org.jk.eSked.model.User;
import org.jk.eSked.model.event.Event;
import org.jk.eSked.services.emailService.EmailService;
import org.jk.eSked.services.events.EventService;
import org.jk.eSked.services.groups.GroupsService;
import org.jk.eSked.services.users.UserService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Route(value = "login")
@PageTitle("Logowanie")
@PWA(name = "eSked - schedule app!", shortName = "eSked", iconPath = "img/icons/logo.png")
class LoginView extends VerticalLayout {

    private final UserService userService;

    public LoginView(UserService userService, GroupsService groupsService, EventService eventService, EmailService emailService) {
        this.userService = userService;

        if (VaadinSession.getCurrent().getSession() == null) VaadinSession.getCurrent().close();

        add(loginLayout(eventService, groupsService, emailService));
    }

    private LoginOverlay loginLayout(EventService eventService, GroupsService groupsService, EmailService emailService) {
        LoginOverlay loginOverlay = new LoginOverlay();
        H1 title = new H1();
        title.getStyle().set("color", "var(--lumo-base-color)");
        Icon icon = VaadinIcon.CALENDAR.create();
        icon.setSize("30px");
        icon.getStyle().set("top", "-4px");
        title.add(icon);
        title.add(new Text(" eSked"));
        loginOverlay.setTitle(title);

        loginOverlay.setTitle(title);
        loginOverlay.addLoginListener(login -> {
            User user = authenticate(login.getUsername(), login.getPassword());
            if (user != null) {
                loginOverlay.setOpened(false);
                VaadinSession.getCurrent().setAttribute(User.class, user);
                if (user.getGroupCode() != 0) {
                    groupsService.synchronizeWGroup(user.getId(), user.getGroupCode());
                }
                Collection<Event> events = eventService.getAllEvents(user.getId());
                List<Notification> notifications = new ArrayList<>();
                for (Event event : events) {
                    if (event.getCreatedDate().isAfter(user.getLastLoggedDate())) {
                        notifications.add(new Notification("Temat: " + event.getTopic(), event.getDate()));
                    }
                }
                VaadinSession.getCurrent().setAttribute(Collection.class, notifications);
                userService.setLastLogged(user.getId(), Instant.now().toEpochMilli());

                UI.getCurrent().navigate("schedule");
                if (user.isDarkTheme())
                    UI.getCurrent().getPage().executeJs("document.documentElement.setAttribute(\"theme\",\"dark\")");
                else
                    UI.getCurrent().getPage().executeJs("document.documentElement.setAttribute(\"theme\",\"white\")");
            } else {
                loginOverlay.setError(true);
            }
        });
        loginOverlay.addForgotPasswordListener(forgotPasswordEvent -> {
            ProblemDialog problemDialog = new ProblemDialog(userService, emailService);
            problemDialog.open();
        });

        loginOverlay.setI18n(createPolishI18n());
        loginOverlay.setOpened(true);
        return loginOverlay;
    }

    private User authenticate(String input, String password) {
        Collection<User> users = userService.getUsers();
        for (User user : users)
            if (user.getUsername().equals(input) || user.getEmail().equals(input))
                if (user.getPassword().equals(User.encodePassword(password))) return user;
        return null;
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

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        CheckTimeTheme checkTimeTheme = new CheckTimeTheme();
        checkTimeTheme.check();
    }
}