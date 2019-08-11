package org.jk.eSked.view;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.jk.eSked.services.LoginService;
import org.jk.eSked.view.menu.MenuView;

@SuppressWarnings("unused")
@Route(value = "aboutapp", layout = MenuView.class)
@PageTitle("O Aplikacji")
public class AboutAppView extends VerticalLayout {
    public AboutAppView(LoginService loginService) {
        if (loginService.checkIfUserIsLogged()) {
            Label text = new Label("[...]");

            add(text);
        }
    }
}