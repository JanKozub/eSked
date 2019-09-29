package org.jk.eSked.ui.views.admin.users;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.jk.eSked.app.LoginService;
import org.jk.eSked.backend.model.User;
import org.jk.eSked.backend.service.*;
import org.jk.eSked.ui.MenuView;
import org.jk.eSked.ui.components.myImpl.AdminReturnButton;

@SuppressWarnings("unused")
@Route(value = "admin/users", layout = MenuView.class)
@PageTitle("Użytkownicy")
class UsersPageView extends VerticalLayout {

    UsersPageView(LoginService loginService, ScheduleService scheduleService, EventService eventService, UserService userService, HoursService hoursService, EmailService emailService) {

        if (loginService.checkIfUserIsLogged()) {
            if (loginService.checkIfUserIsLoggedAsAdmin()) {
                Grid<User> userGrid = new Grid<>();
                userGrid.setHeightByRows(true);
                userGrid.setSelectionMode(Grid.SelectionMode.NONE);
                userGrid.addColumn(User::getUsername).setHeader("Nazwa użytkownika");
                userGrid.addColumn(User::getLastLoggedDate).setHeader("Ostatnio zalogowany");
                userGrid.addColumn(User::getId).setHeader("ID");
                userGrid.addColumn(new ComponentRenderer<>(user -> {
                    Button button = new Button("Usuń", event -> {
                        userService.deleteUser(user.getId());
                        userGrid.setItems(userService.getUsers());
                    });
                    button.getStyle().set("color", "red");
                    return new HorizontalLayout(button);
                })).setFlexGrow(0);
                userGrid.setItems(userService.getUsers());
                userGrid.setRowsDraggable(false);
                userGrid.getColumns().forEach(column -> column.setAutoWidth(true));
                add(new AdminReturnButton(), userGrid);
            }
        }
    }
}