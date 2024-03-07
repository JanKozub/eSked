package org.jk.eSked.ui.views.login;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.jk.eSked.app.security.SecurityUtils;
import org.jk.eSked.backend.service.EmailService;
import org.jk.eSked.backend.service.user.GroupService;
import org.jk.eSked.backend.service.user.UserService;
import org.springframework.security.authentication.AuthenticationManager;

@Route(value = "login")
@PageTitle("Login")
public class LoginView extends VerticalLayout {
    public LoginView(AuthenticationManager authManager, UserService userService, GroupService groupService, EmailService emailService) {
        add(new MyLoginOverlay(authManager, userService, groupService, emailService));

        if (SecurityUtils.isUserLoggedIn()) {
            UI.getCurrent().navigate("schedule");
            UI.getCurrent().getPage().reload();
        }
    }
}
