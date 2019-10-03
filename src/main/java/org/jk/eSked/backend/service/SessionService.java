package org.jk.eSked.backend.service;

import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WebBrowser;
import org.jk.eSked.backend.model.User;

import java.util.UUID;

public class SessionService {

    public static VaadinSession getSession() {
        return VaadinSession.getCurrent();
    }

    public static WebBrowser getBrowser() {
        return VaadinSession.getCurrent().getBrowser();
    }

    public static UUID getUserId() {
        return VaadinSession.getCurrent().getAttribute(User.class).getId();
    }

    public static boolean isSessionMoblie() {
        return VaadinSession.getCurrent().getBrowser().getBrowserApplication().contains("Mobile");
    }
}
