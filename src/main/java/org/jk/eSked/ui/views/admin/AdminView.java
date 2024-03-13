package org.jk.eSked.ui.views.admin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.ui.views.MainLayout;
import org.springframework.security.access.annotation.Secured;

@Route(value = "admin", layout = MainLayout.class)
@Secured("ROLE_ADMIN")
public class AdminView extends HorizontalLayout implements HasDynamicTitle {
    public AdminView() {
        SessionService.setAutoTheme();

        Button user = createButton(getTranslation("user"), "admin/user", VaadinIcon.USER);
        Button users = createButton(getTranslation("page.users"), "admin/users", VaadinIcon.USERS);
        Button groups = createButton(getTranslation("page.groups"), "admin/groups", VaadinIcon.FORM);
        Button groupsAcc = createButton(getTranslation("page.confirm.groups"), "admin/groups/pending", VaadinIcon.LIST_SELECT);

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

    @Override
    public String getPageTitle() {
        return getTranslation("page.administrator.panel");
    }
}
