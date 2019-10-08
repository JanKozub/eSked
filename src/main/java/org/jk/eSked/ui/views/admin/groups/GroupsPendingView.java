package org.jk.eSked.ui.views.admin.groups;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.jk.eSked.backend.model.Group;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.backend.service.user.GroupService;
import org.jk.eSked.backend.service.user.ScheduleService;
import org.jk.eSked.backend.service.user.UserService;
import org.jk.eSked.ui.components.menu.Menu;
import org.jk.eSked.ui.components.myImpl.AdminReturnButton;
import org.jk.eSked.ui.components.schedule.ScheduleGridNewEntries;
import org.springframework.security.access.annotation.Secured;

import java.util.Collection;

@Route(value = "admin/groups/pending", layout = Menu.class)
@PageTitle("Zatwierdzanie Grup")
@Secured("ROLE_ADMIN")
class GroupsPendingView extends VerticalLayout {
    private final ScheduleService scheduleService;
    private final GroupService groupsService;
    private final UserService userService;

    public GroupsPendingView(ScheduleService scheduleService, GroupService groupsService, UserService userService) {
        this.scheduleService = scheduleService;
        this.groupsService = groupsService;
        this.userService = userService;
        SessionService.setAutoTheme();

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
                add(groupLayout());
            });
            return button;
        })).setHeader("Szczegóły");
        groupEntryGrid.addColumn(new ComponentRenderer<>(e -> {
            Button button = new Button("Akceptuj");
            button.getStyle().set("color", "green");
            button.addClickListener(event -> {
                groupsService.setGroupAccepted(e.getGroupCode());
                userService.setGroupCode(e.getLeaderId(), e.getGroupCode());
                Collection<Group> groups = groupsService.getGroups();
                groups.removeIf(Group::isAccepted);
                groupEntryGrid.setDataProvider(new ListDataProvider<>(groups));
            });
            return button;
        })).setHeader("Akceptuj");
        groupEntryGrid.addColumn(new ComponentRenderer<>(e -> {
            Button button = new Button("Odrzuć");
            button.getStyle().set("color", "red");
            button.addClickListener(event -> {
                groupsService.deleteGroup(e.getGroupCode());
                Collection<Group> groups = groupsService.getGroups();
                groups.removeIf(Group::isAccepted);
                groupEntryGrid.setDataProvider(new ListDataProvider<>(groups));
            });
            return button;
        })).setHeader("Odrzuć");

        Collection<Group> groups = groupsService.getGroups();
        groups.removeIf(Group::isAccepted);
        groupEntryGrid.setItems(groups);
        groupEntryGrid.setSizeFull();
        groupEntryGrid.getColumns().forEach(column -> column.setAutoWidth(true));
        setSizeFull();
        return new VerticalLayout(new AdminReturnButton(), groupEntryGrid);
    }

    private VerticalLayout groupLayout() {
        Button button = new Button("Powrót", event -> {
            removeAll();
            VerticalLayout layout = mainLayout();
            layout.setSizeFull();
            add(layout);
        });
        button.setWidth("100%");
        ScheduleGridNewEntries scheduleGridNewEntries = new ScheduleGridNewEntries(scheduleService, userService);

        return new VerticalLayout(button, scheduleGridNewEntries);
    }
}
