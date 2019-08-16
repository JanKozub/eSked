package org.jk.eSked.view.admin.groups;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.jk.eSked.components.AdminReturnButton;
import org.jk.eSked.components.EventGrid;
import org.jk.eSked.components.schedule.ScheduleGridNewEntries;
import org.jk.eSked.model.Group;
import org.jk.eSked.model.User;
import org.jk.eSked.services.LoginService;
import org.jk.eSked.services.TimeService;
import org.jk.eSked.services.events.EventService;
import org.jk.eSked.services.groups.GroupsService;
import org.jk.eSked.services.schedule.ScheduleService;
import org.jk.eSked.services.users.UserService;
import org.jk.eSked.view.menu.MenuView;

import java.util.Collection;

@Route(value = "admin/groups", layout = MenuView.class)
@PageTitle("Zatwierdzanie Grup")
class GroupsView extends VerticalLayout {
    private final ScheduleService scheduleService;
    private final TimeService timeService;
    private final GroupsService groupsService;
    private final EventService eventService;
    private final UserService userService;

    public GroupsView(LoginService loginService, ScheduleService scheduleService, TimeService timeService, GroupsService groupsService, EventService eventService, UserService userService) {
        this.scheduleService = scheduleService;
        this.timeService = timeService;
        this.groupsService = groupsService;
        this.eventService = eventService;
        this.userService = userService;

        if (loginService.checkIfUserIsLogged()) {
            if (loginService.checkIfUserIsLoggedAsAdmin()) {
                VerticalLayout layout = mainLayout();
                layout.setSizeFull();
                add(layout);
            }
        }
    }

    private VerticalLayout mainLayout() {
        Grid<Group> groupEntryGrid = new Grid<>();
        groupEntryGrid.addColumn(Group::getName).setHeader("Nazwa");
        groupEntryGrid.addColumn(Group::getCode).setHeader("Kod Grupy");
        groupEntryGrid.addColumn(new ComponentRenderer<>(e -> {
            Button button = new Button("Szczegóły/Edycja");
            button.addClickListener(event -> {
                removeAll();
                add(groupLayout(e.getCode()));
            });
            return button;
        })).setHeader("Szczegóły");
        groupEntryGrid.addColumn(new ComponentRenderer<>(e -> {
            Button button = new Button("Usuń");
            button.getStyle().set("color", "red");
            button.addClickListener(event -> {
                groupsService.deleteGroup(e.getCode());
                Collection<User> users = userService.getUsers();
                for (User user : users) {
                    if (user.getGroupCode() == e.getCode()) {
                        userService.setGroupCode(user.getId(), 0);
                    }
                }
                Collection<Group> groups = groupsService.getGroups();
                groups.removeIf(group -> !group.isAccepted());
                groupEntryGrid.setItems(groups);
            });
            return button;
        })).setHeader("Usuń");
        Collection<Group> groups = groupsService.getGroups();
        groups.removeIf(group -> !group.isAccepted());
        groupEntryGrid.setItems(groups);
        groupEntryGrid.setSizeFull();
        groupEntryGrid.getColumns().forEach(column -> column.setAutoWidth(true));
        setSizeFull();
        return new VerticalLayout(new AdminReturnButton(), groupEntryGrid);
    }

    private VerticalLayout groupLayout(int groupCode) {
        Button button = new Button("Powrót", event -> {
            removeAll();
            VerticalLayout layout = mainLayout();
            layout.setSizeFull();
            add(layout);
        });
        button.setWidth("100%");
        ScheduleGridNewEntries scheduleGridNewEntries = new ScheduleGridNewEntries(scheduleService, groupsService, userService, timeService, groupCode);
        EventGrid eventGrid = new EventGrid(scheduleService, eventService, timeService, groupsService.getLeaderId(groupCode));
        return new VerticalLayout(button, scheduleGridNewEntries, eventGrid);
    }
}