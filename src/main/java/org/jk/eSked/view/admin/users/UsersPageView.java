package org.jk.eSked.view.admin.users;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.jk.eSked.component.AdminReturnButton;
import org.jk.eSked.model.User;
import org.jk.eSked.services.TimeService;
import org.jk.eSked.services.events.EventService;
import org.jk.eSked.services.groups.GroupsService;
import org.jk.eSked.services.schedule.ScheduleService;
import org.jk.eSked.services.users.UserService;
import org.jk.eSked.services.LoginService;
import org.jk.eSked.view.menu.MenuView;

@SuppressWarnings("unused")
@Route(value = "admin/users", layout = MenuView.class)
@PageTitle("Użytkownicy")
class UsersPageView extends VerticalLayout {

    UsersPageView(LoginService loginService, ScheduleService scheduleService, EventService eventService, UserService userService, GroupsService groupsService, TimeService timeService) {

        if (loginService.checkIfUserIsLogged()) {
            if (loginService.checkIfUserIsLoggedAsAdmin()) {
                Grid<User> userGrid = new Grid<>();
                userGrid.setHeightByRows(true);
                userGrid.setSelectionMode(Grid.SelectionMode.NONE);
                userGrid.addColumn(User::getUsername).setHeader("Nazwa użytkownika").setResizable(true);
                userGrid.addColumn(User::getLastLoggedDate).setHeader("Ostatnio zalogowany").setResizable(true);
                userGrid.addColumn(User::getId).setHeader("ID").setResizable(true);
                userGrid.addColumn(new ComponentRenderer<>(user -> {
                    Button button = new Button("Szczegóły", event -> {
                        removeAll();
                        UserFoundView userFoundView = new UserFoundView(loginService, scheduleService, eventService, userService, groupsService, timeService, user);
                        add(userFoundView);
                    });
                    return new HorizontalLayout(button);
                }));
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
                add(new AdminReturnButton(), userGrid);
            }
        }
    }
}