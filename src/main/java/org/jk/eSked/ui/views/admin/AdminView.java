package org.jk.eSked.ui.views.admin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.ui.views.MainLayout;
import org.springframework.security.access.annotation.Secured;

@Route(value = "admin", layout = MainLayout.class)
@PageTitle("Panel Admininstratora")
@Secured("ROLE_ADMIN")
public class AdminView extends HorizontalLayout {

    public AdminView() {
        SessionService.setAutoTheme();

        Button user = createButton("Użytkownik", "admin/user", VaadinIcon.USER);
        Button users = createButton("Użytkownicy", "admin/users", VaadinIcon.USERS);
        Button groups = createButton("Grupy", "admin/groups", VaadinIcon.FORM);
        Button groupsAcc = createButton("Zatwierdzanie Grup", "admin/groups/pending", VaadinIcon.LIST_SELECT);

        FormLayout formLayout = new FormLayout(user, users, groups, groupsAcc);
        formLayout.getStyle().set("padding-top", "5px").set("padding-left", "20px").set("padding-right", "20px");
        formLayout.setSizeFull();

        setAlignItems(Alignment.CENTER);
        add(formLayout);
    }

    private Button createButton(String label, String navigationRoute, VaadinIcon icon) {
        Icon iconUser = new Icon(icon);
        Button button = new Button(label, iconUser, buttonClickEvent -> UI.getCurrent().navigate(navigationRoute));
        button.getStyle().set("height", "100px").set("margin-top", "15px");
        return button;
    }
}
