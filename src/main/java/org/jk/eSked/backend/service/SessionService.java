package org.jk.eSked.backend.service;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.backend.model.User;
import org.jk.eSked.backend.service.user.UserService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SessionService {

    private static UserService userService;

    public SessionService(UserService userService) {
        SessionService.userService = userService;
    }

    public static UUID getUserId() {
        try {
            VaadinSession.getCurrent().getLockInstance().lock();
            return VaadinSession.getCurrent().getAttribute(User.class).getId();
        } finally {
            VaadinSession.getCurrent().getLockInstance().unlock();
        }
    }

    public static boolean isSessionMobile() {
        return VaadinSession.getCurrent().getBrowser().getBrowserApplication().contains("Mobile");
    }

    public static void setAutoTheme() {
        UI.getCurrent().getPage()
                .executeJs("document.documentElement.setAttribute(\"theme\",\"" + userService.getTheme(getUserId()).toString().toLowerCase() + "\")");
    }

}
