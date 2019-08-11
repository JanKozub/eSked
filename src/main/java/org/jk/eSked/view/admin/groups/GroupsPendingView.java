package org.jk.eSked.view.admin.groups;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.jk.eSked.component.AdminReturnButton;
import org.jk.eSked.component.schedule.ScheduleGridNewEntries;
import org.jk.eSked.model.Group;
import org.jk.eSked.services.TimeService;
import org.jk.eSked.services.groups.GroupsService;
import org.jk.eSked.services.schedule.ScheduleService;
import org.jk.eSked.services.users.UserService;
import org.jk.eSked.services.LoginService;
import org.jk.eSked.view.menu.MenuView;

import java.util.Collection;

@Route(value = "admin/groups/pending", layout = MenuView.class)
@PageTitle("Zatwierdzanie Grup")
class GroupsPendingView extends VerticalLayout {
    private final ScheduleService scheduleService;
    private final TimeService timeService;
    private final GroupsService groupsService;
    private final UserService userService;


    public GroupsPendingView(LoginService loginService, ScheduleService scheduleService, TimeService timeService, GroupsService groupsService, UserService userService) {
        this.scheduleService = scheduleService;
        this.timeService = timeService;
        this.groupsService = groupsService;
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
            Button button = new Button("Akceptuj");
            button.getStyle().set("color", "green");
            button.addClickListener(event -> {
                groupsService.setGroupAccepted(e.getCode());
                Collection<Group> groups = groupsService.getGroups();
                groups.removeIf(Group::isAccepted);
                groupEntryGrid.setDataProvider(new ListDataProvider<>(groups));
            });
            return button;
        })).setHeader("Akceptuj");
        Collection<Group> groups = groupsService.getGroups();
        groups.removeIf(Group::isAccepted);
        groupEntryGrid.setItems(groups);
        groupEntryGrid.setSizeFull();
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

        return new VerticalLayout(button, scheduleGridNewEntries);
    }
}