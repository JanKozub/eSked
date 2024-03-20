package org.jk.esked.app.frontend.views.admin.users;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.jk.esked.app.backend.model.entities.User;
import org.jk.esked.app.backend.model.types.UserType;
import org.jk.esked.app.backend.services.UserService;
import org.jk.esked.app.backend.services.utilities.TimeService;
import org.jk.esked.app.frontend.components.admin.AdminReturnButton;
import org.jk.esked.app.frontend.views.MainLayout;

@Route(value = "admin/users", layout = MainLayout.class)
@RolesAllowed("ADMIN")
class UsersPageView extends VerticalLayout implements HasDynamicTitle {
    public UsersPageView(UserService userService) {
        Grid<User> userGrid = new Grid<>();
        userGrid.setAllRowsVisible(true);
        userGrid.setSelectionMode(Grid.SelectionMode.NONE);
        userGrid.addColumn(User::getUsername).setHeader(getTranslation("username"));
        userGrid.addColumn(User::getEmail).setHeader("E-Mail");
        userGrid.addColumn(User::getGroupCode).setHeader(getTranslation("group"));
        userGrid.addColumn(u -> TimeService.timestampToFormatedString(u.getLastLoggedTimestamp())).setHeader(getTranslation("users.last.logged"));
        userGrid.addColumn(u -> TimeService.timestampToFormatedString(u.getCreatedTimestamp())).setHeader(getTranslation("date.created"));
        userGrid.addColumn(User::isVerified).setHeader(getTranslation("verified"));
        userGrid.addColumn(new ComponentRenderer<>(user ->
                new Button(user.getUserType().getDescription(),
                        e -> {
                            userService.changeUserTypeById(user.getId(),
                                    user.getUserType() == UserType.USER ? UserType.ADMIN : UserType.USER);
                            UserType userType = userService.getUserTypeByUserId(user.getId());
                            user.setUserType(userType);
                            e.getSource().setText(userType.getDescription());
                        }))).setFlexGrow(0).setHeader(getTranslation("type"));
        userGrid.addColumn(new ComponentRenderer<>(user -> {
            Button button = new Button(getTranslation("groups.details"), event -> {
                //TODO implement
            });
            return new HorizontalLayout(button);
        })).setFlexGrow(0);
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
