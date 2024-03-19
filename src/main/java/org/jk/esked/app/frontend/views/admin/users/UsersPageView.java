package org.jk.esked.app.frontend.views.admin.users;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.jk.esked.app.backend.model.User;
import org.jk.esked.app.backend.services.UserService;
import org.jk.esked.app.frontend.components.AdminReturnButton;
import org.jk.esked.app.frontend.views.MainLayout;

@Route(value = "admin/users", layout = MainLayout.class)
@RolesAllowed("ADMIN")
class UsersPageView extends VerticalLayout implements HasDynamicTitle {
    public UsersPageView(UserService userService) {
        Grid<User> userGrid = new Grid<>();
        userGrid.setAllRowsVisible(true);
        userGrid.setSelectionMode(Grid.SelectionMode.NONE);
        userGrid.addColumn(User::getUsername).setHeader(getTranslation("username"));
        userGrid.addColumn(User::getLastLoggedTimestamp).setHeader(getTranslation("users.last.logged"));
        userGrid.addColumn(User::getId).setHeader("ID");
        userGrid.addColumn(new ComponentRenderer<>(user -> {
            Button button = new Button(getTranslation("delete"), event -> {
                userService.deleteUser(user.getId());
                userGrid.setItems(userService.getAllUsers());
            });
            button.getStyle().set("color", "red");
            return new HorizontalLayout(button);
        })).setFlexGrow(0);
        userGrid.setItems(userService.getAllUsers());
        userGrid.setRowsDraggable(false);
        userGrid.getColumns().forEach(column -> column.setAutoWidth(true));
        add(new AdminReturnButton(), userGrid);
    }

    @Override
    public String getPageTitle() {
        return getTranslation("page.users");
    }
}
