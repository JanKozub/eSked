package org.jk.esked.app.ui.views.admin.groups;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.jk.esked.app.backend.model.Group;
import org.jk.esked.app.backend.model.User;
import org.jk.esked.app.backend.security.SecurityService;
import org.jk.esked.app.backend.services.GroupService;
import org.jk.esked.app.backend.services.ScheduleService;
import org.jk.esked.app.backend.services.UserService;
import org.jk.esked.app.ui.components.AdminReturnButton;
import org.jk.esked.app.ui.components.admin.GroupsGrid;
import org.jk.esked.app.ui.views.MainLayout;

import java.util.Collection;

@Route(value = "admin/groups", layout = MainLayout.class)
@RolesAllowed("ADMIN")
class GroupsView extends VerticalLayout implements HasDynamicTitle {

    public GroupsView(ScheduleService scheduleService, GroupService groupsService, UserService userService, SecurityService securityService) {
        GroupsGrid groupEntryGrid = new GroupsGrid(securityService.getUser(), scheduleService, userService);

        groupEntryGrid.addColumn(new ComponentRenderer<>(e -> {
            Button button = new Button(getTranslation("delete"));
            button.getStyle().set("color", "red");
            button.addClickListener(event -> {
                groupsService.deleteGroupByGroupCode(e.getGroupCode());
                Collection<User> users = userService.getAllUsers();
                for (User user : users) {
                    if (user.getGroupCode() == e.getGroupCode()) {
                        userService.changeGroupCodeByUserId(user.getId(), 0);
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
        return  getTranslation("page.groups");
    }
}
