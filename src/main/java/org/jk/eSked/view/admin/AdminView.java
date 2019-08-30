package org.jk.eSked.view.admin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.jk.eSked.services.LoginService;
import org.jk.eSked.view.MenuView;

@Route(value = "admin", layout = MenuView.class)
@PageTitle("Panel Admininstratora")
public class AdminView extends HorizontalLayout {

    public AdminView(LoginService loginService) {

        if (loginService.checkIfUserIsLogged()) {
            if (loginService.checkIfUserIsLoggedAsAdmin()) {
                Icon iconUser = new Icon(VaadinIcon.USER);
                Button user = new Button("Użytkownik", iconUser, buttonClickEvent -> UI.getCurrent().navigate("admin/user"));
                user.setHeight("300%");

                Icon iconUsers = new Icon(VaadinIcon.USERS);
                Button users = new Button("Użytkownicy", iconUsers, buttonClickEvent -> UI.getCurrent().navigate("admin/users"));

                Icon iconGroups = new Icon(VaadinIcon.FORM);
                Button groups = new Button("Grupy", iconGroups, buttonClickEvent -> UI.getCurrent().navigate("admin/groups"));

                Icon iconGroupsAcc = new Icon(VaadinIcon.LIST_SELECT);
                Button groupsAcc = new Button("Zatwierdzanie Grup", iconGroupsAcc, buttonClickEvent -> UI.getCurrent().navigate("admin/groups/pending"));

                FormLayout formLayout = new FormLayout(user, users, groups, groupsAcc);
                formLayout.setSizeFull();

                setSizeFull();
                setAlignItems(Alignment.CENTER);
                add(formLayout);
            }
        }
    }
}
