package org.jk.eSked.services;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.model.User;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    public boolean checkIfUserIsLogged() {
        try {
            if (VaadinSession.getCurrent().getAttribute(User.class).getId() == null)
                throw new NullPointerException();
        } catch (NullPointerException ex) {
            UI.getCurrent().navigate("login");
            UI.getCurrent().getPage().reload();
            return false;
        }
        return true;
    }

    public boolean checkIfUserIsLoggedAsAdmin() {
        try {
            if (!VaadinSession.getCurrent().getAttribute(User.class).getUsername().equals("admin"))
                throw new NullPointerException();
            else return true;
        } catch (NullPointerException ex) {
            return false;
        }
    }
}
