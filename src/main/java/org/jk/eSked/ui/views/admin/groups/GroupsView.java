package org.jk.eSked.ui.views.admin.groups;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.backend.model.Group;
import org.jk.eSked.backend.model.User;
import org.jk.eSked.backend.service.user.GroupService;
import org.jk.eSked.backend.service.user.ScheduleService;
import org.jk.eSked.backend.service.user.UserService;
import org.jk.eSked.ui.components.admin.GroupsGrid;
import org.jk.eSked.ui.components.myComponents.AdminReturnButton;
import org.jk.eSked.ui.views.MainLayout;
import org.springframework.security.access.annotation.Secured;

import java.util.Collection;
import java.util.Locale;

@Route(value = "admin/groups", layout = MainLayout.class)
@Secured("ROLE_ADMIN")
class GroupsView extends VerticalLayout implements HasDynamicTitle {
    private final static Locale locale = VaadinSession.getCurrent().getLocale();

    public GroupsView(ScheduleService scheduleService, GroupService groupsService, UserService userService) {
        GroupsGrid groupEntryGrid = new GroupsGrid(scheduleService, userService);

        groupEntryGrid.addColumn(new ComponentRenderer<>(e -> {
            Button button = new Button(getTranslation(locale, "delete"));
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
        })).setHeader(getTranslation(locale, "delete"));
        Collection<Group> groups = groupsService.getGroups();
        groups.removeIf(group -> !group.isAccepted());
        groupEntryGrid.setItems(groups);
        groupEntryGrid.setSizeFull();
        groupEntryGrid.getColumns().forEach(column -> column.setAutoWidth(true));

        setSizeFull();
        add(new AdminReturnButton(), groupEntryGrid);
    }

    @Override
    public String getPageTitle() {
        return  getTranslation(locale, "page.groups");
    }
}
