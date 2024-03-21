package org.jk.esked.app.frontend.views.admin.groups;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.jk.esked.app.backend.model.entities.Group;
import org.jk.esked.app.backend.model.entities.User;
import org.jk.esked.app.backend.security.SecurityService;
import org.jk.esked.app.backend.services.GroupService;
import org.jk.esked.app.backend.services.HourService;
import org.jk.esked.app.backend.services.ScheduleEntryService;
import org.jk.esked.app.backend.services.UserService;
import org.jk.esked.app.frontend.components.admin.AdminReturnButton;
import org.jk.esked.app.frontend.components.admin.GroupsGrid;
import org.jk.esked.app.frontend.views.MainLayout;

import java.util.Collection;

@Route(value = "admin/groups", layout = MainLayout.class)
@RolesAllowed("ADMIN")
class GroupsView extends VerticalLayout implements HasDynamicTitle {

    public GroupsView(ScheduleEntryService scheduleEntryService, GroupService groupsService, UserService userService, SecurityService securityService, HourService hourService) {
        GroupsGrid groupEntryGrid = new GroupsGrid(securityService.getUser(), scheduleEntryService, userService, hourService);

        groupEntryGrid.addColumn(new ComponentRenderer<>(e -> {
            Button button = new Button(getTranslation("delete"));
            button.getStyle().set("color", "red");
            button.addClickListener(event -> {
                groupsService.deleteGroupByGroupCode(e.getGroupCode());
                Collection<User> users = userService.findAllUsers();
                for (User user : users) {
                    if (user.getGroupCode() == e.getGroupCode()) {
                        userService.changeGroupCodeById(user.getId(), 0);
                    }
                }
                Collection<Group> groups = groupsService.getAllGroups();
                groups.removeIf(group -> !group.isAccepted());
                groupEntryGrid.setItems(groups);
            });
            return button;
        })).setHeader(getTranslation("delete"));
        Collection<Group> groups = groupsService.getAllGroups();
        groups.removeIf(group -> !group.isAccepted());
        groupEntryGrid.setItems(groups);
        groupEntryGrid.setSizeFull();
        groupEntryGrid.getColumns().forEach(column -> column.setAutoWidth(true));

        setSizeFull();
        add(new AdminReturnButton(), groupEntryGrid);
    }

    @Override
    public String getPageTitle() {
        return getTranslation("page.groups");
    }
}
