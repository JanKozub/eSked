package org.jk.eSked.view;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.components.CheckTimeTheme;
import org.jk.eSked.components.dialogs.InfoDialog;
import org.jk.eSked.components.dialogs.NewUserDialog;
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
@PWA(name = "Web schedule for students!", shortName = "eSked", iconPath = "img/icons/logo.png")
class LoginView extends VerticalLayout {

    private final TextField usernameField;
    private final PasswordField passwordField;
    private final UserService userService;
    private final GroupsService groupsService;
    private final EventService eventService;

    public LoginView(UserService userService, GroupsService groupsService, EventService eventService, EmailService emailService) {
        this.userService = userService;
        this.groupsService = groupsService;
        this.eventService = eventService;

        if (VaadinSession.getCurrent().getSession() == null) VaadinSession.getCurrent().close();

        Icon icon = new Icon(VaadinIcon.USER);
        usernameField = new TextField("Nazwa użytkownika:");

        passwordField = new PasswordField("Hasło:");
        passwordField.setErrorMessage("Nazwa użytkownika lub hasło niepoprawne. Spróbuj ponownie");

        Button loginButton = new Button("Zaloguj!", click -> login(usernameField.getValue(), passwordField.getValue()));
        loginButton.addClickShortcut(Key.ENTER);

        NewUserDialog newUserDialog = new NewUserDialog(userService, emailService);
        Icon newUser = new Icon(VaadinIcon.PLUS_CIRCLE);
        newUser.setSize("25px");
        newUser.getStyle().set("cursor", "pointer");
        newUser.addClickListener(e -> newUserDialog.open());

        Icon forgotPass = new Icon(VaadinIcon.WRENCH);
        forgotPass.setSize("25px");
        forgotPass.getStyle().set("cursor", "pointer");
        ProblemDialog problemDialog = new ProblemDialog(userService, emailService);
        forgotPass.addClickListener(e -> problemDialog.open());

        Icon info = new Icon(VaadinIcon.INFO_CIRCLE);
        info.setSize("25px");
        info.getStyle().set("cursor", "pointer");
        InfoDialog infoDialog = new InfoDialog();
        info.addClickListener(e -> infoDialog.open());

        VerticalLayout icons = new VerticalLayout(newUser, forgotPass, info);
        icons.setAlignItems(Alignment.END);

        VerticalLayout mainLayout = new VerticalLayout(icon, usernameField, passwordField, loginButton);
        mainLayout.setAlignItems(Alignment.CENTER);
        mainLayout.getStyle().set("margin-top", "-100px");

        add(icons, mainLayout);
    }

    private void login(String uTyped, String pTyped) {
        pTyped = User.encodePassword(pTyped);

        Collection<User> users = userService.getUsers();
        boolean userFound = false;
        for (User user : users) {
            if (user.getUsername().equals(uTyped) && user.getPassword().equals(pTyped)) {
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
                userFound = true;
            }
        }
        if (!userFound) {
            usernameField.setInvalid(true);
            passwordField.setInvalid(true);
            passwordField.setValue("");
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        CheckTimeTheme checkTimeTheme = new CheckTimeTheme();
        checkTimeTheme.check();
    }

}