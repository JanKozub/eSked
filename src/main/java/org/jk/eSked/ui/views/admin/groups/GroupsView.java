package org.jk.eSked.ui.views.admin.groups;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.jk.eSked.backend.model.Group;
import org.jk.eSked.backend.model.User;
import org.jk.eSked.backend.service.EventService;
import org.jk.eSked.backend.service.GroupService;
import org.jk.eSked.backend.service.ScheduleService;
import org.jk.eSked.backend.service.UserService;
import org.jk.eSked.ui.MenuView;
import org.jk.eSked.ui.components.myImpl.AdminReturnButton;
import org.jk.eSked.ui.components.schedule.EventGrid;
import org.jk.eSked.ui.components.schedule.ScheduleGridNewEntries;

import java.util.Collection;

@Route(value = "admin/groups", layout = MenuView.class)
@PageTitle("Zatwierdzanie Grup")
class GroupsView extends VerticalLayout {
    private final ScheduleService scheduleService;
    private final GroupService groupsService;
    private final EventService eventService;
    private final UserService userService;

    public GroupsView(ScheduleService scheduleService, GroupService groupsService, EventService eventService, UserService userService) {
        this.scheduleService = scheduleService;
        this.groupsService = groupsService;
        this.eventService = eventService;
        this.userService = userService;

        VerticalLayout layout = mainLayout();
        layout.setSizeFull();
        add(layout);

    }

    private VerticalLayout mainLayout() {
        Grid<Group> groupEntryGrid = new Grid<>();
        groupEntryGrid.addColumn(Group::getName).setHeader("Nazwa");
        groupEntryGrid.addColumn(Group::getGroupCode).setHeader("Kod Grupy");
        groupEntryGrid.addColumn(new ComponentRenderer<>(e -> {
            Button button = new Button("Szczegóły/Edycja");
            button.addClickListener(event -> {
                removeAll();
                add(groupLayout(e.getGroupCode()));
            });
            return button;
        })).setHeader("Szczegóły");
        groupEntryGrid.addColumn(new ComponentRenderer<>(e -> {
            Button button = new Button("Usuń");
            button.getStyle().set("color", "red");
            button.addClickListener(event -> {
                groupsService.deleteGroup(e.getGroupCode());
                Collection<User> users = userService.getUsers();
                for (User user : users) {
                    if (user.getGroupCode() == e.getGroupCode()) {
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
        ScheduleGridNewEntries scheduleGridNewEntries = new ScheduleGridNewEntries(scheduleService, userService);
        EventGrid eventGrid = new EventGrid(scheduleService, eventService, groupsService.getLeaderId(groupCode));
        return new VerticalLayout(button, scheduleGridNewEntries, eventGrid);
    }
}