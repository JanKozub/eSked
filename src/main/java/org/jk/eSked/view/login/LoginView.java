package org.jk.eSked.view.login;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.component.CheckTimeTheme;
import org.jk.eSked.component.InfoDialog;
import org.jk.eSked.component.SimplePopup;
import org.jk.eSked.model.User;
import org.jk.eSked.services.GroupSynService;
import org.jk.eSked.services.users.UserService;

import java.time.Instant;
import java.util.Collection;

@SuppressWarnings("unused")
@Route(value = "login")
@PageTitle("Logowanie")
class LoginView extends VerticalLayout {

    private final TextField usernameTyped;
    private final PasswordField passwordTyped;
    private final UserService userService;
    private final GroupSynService groupSynService;

    public LoginView(UserService userService, GroupSynService groupSynService) {
        this.userService = userService;
        this.groupSynService = groupSynService;

        if (VaadinSession.getCurrent().getSession() == null) VaadinSession.getCurrent().close();

        Icon icon = new Icon(VaadinIcon.USER);
        usernameTyped = new TextField("Nazwa użytkownika:");

        passwordTyped = new PasswordField("Hasło:");

        Button loginButton = new Button("Zaloguj!", click -> login(usernameTyped.getValue(), passwordTyped.getValue()));

        NewUserDialog dialog = new NewUserDialog(userService);
        Icon newUser = new Icon(VaadinIcon.PLUS_CIRCLE);
        newUser.setSize("25px");
        newUser.getStyle().set("cursor", "pointer");
        newUser.addClickListener(e -> dialog.open());

        Icon forgotPass = new Icon(VaadinIcon.WRENCH);
        forgotPass.setSize("25px");
        forgotPass.getStyle().set("cursor", "pointer");
        ProblemDialog problemDialog = new ProblemDialog();
        forgotPass.addClickListener(e -> problemDialog.open());

        Icon info = new Icon(VaadinIcon.INFO_CIRCLE);
        info.setSize("25px");
        info.getStyle().set("cursor", "pointer");
        InfoDialog infoDialog = new InfoDialog();
        info.addClickListener(e -> infoDialog.open());

        VerticalLayout icons = new VerticalLayout(newUser, forgotPass, info);
        icons.setAlignItems(Alignment.END);

        VerticalLayout mainLayout = new VerticalLayout(icon, usernameTyped, passwordTyped, loginButton);
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
                userService.setLastLogged(user.getId(), Instant.now().toEpochMilli());
                if (user.getGroupCode() != 0)
                    groupSynService.SynchronizeWGroup(user.getId(), user.getGroupCode());
                UI.getCurrent().navigate("schedule");
                if (user.isDarkTheme())
                    UI.getCurrent().getPage().executeJs("document.documentElement.setAttribute(\"theme\",\"dark\")");
                else
                    UI.getCurrent().getPage().executeJs("document.documentElement.setAttribute(\"theme\",\"white\")");
                userFound = true;
            }
        }
        if (!userFound) {
            SimplePopup popup = new SimplePopup();
            popup.open("Nazwa użytkownika lub hasło niepoprawne!");
            usernameTyped.setValue("");
            passwordTyped.setValue("");
        }

    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        CheckTimeTheme checkTimeTheme = new CheckTimeTheme();
        checkTimeTheme.check();
    }
}