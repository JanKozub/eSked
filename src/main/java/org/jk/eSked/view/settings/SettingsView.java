package org.jk.eSked.view.settings;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.model.User;
import org.jk.eSked.services.LoginService;
import org.jk.eSked.view.menu.MenuView;

@Route(value = "settings", layout = MenuView.class)
@PageTitle("Ustawienia")
public class SettingsView extends VerticalLayout {

    private User user = VaadinSession.getCurrent().getAttribute(User.class);

    public SettingsView(LoginService loginService) {
        if (loginService.checkIfUserIsLogged()) {
            setAlignItems(Alignment.CENTER);
            add(new Settingsbutton("Nazwa", user.getUsername(), "Zmień", SettingsEntryType.USERNAME)

            );
        }
    }
}