package org.jk.esked.app.frontend.views.admin.groups;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.jk.esked.app.backend.model.entities.Group;
import org.jk.esked.app.backend.model.entities.Message;
import org.jk.esked.app.backend.security.SecurityService;
import org.jk.esked.app.backend.services.*;
import org.jk.esked.app.frontend.components.admin.AdminReturnButton;
import org.jk.esked.app.frontend.components.admin.GroupsGrid;
import org.jk.esked.app.frontend.views.MainLayout;

import java.util.List;

@Route(value = "admin/groups/pending", layout = MainLayout.class)
@RolesAllowed("ADMIN")
class GroupsPendingView extends VerticalLayout implements HasDynamicTitle {
    public GroupsPendingView(SecurityService securityService, ScheduleEntryService scheduleEntryService, GroupService groupsService, UserService userService, MessageService messageService, HourService hourService) {
        GroupsGrid groupEntryGrid = new GroupsGrid(securityService.getUser(), scheduleEntryService, userService, hourService);

        groupEntryGrid.addColumn(new ComponentRenderer<>(group -> {
            Button button = new Button(getTranslation("groups.accept"));
            button.getStyle().set("color", "green");
            button.addClickListener(event -> {
                groupsService.changeGroupAcceptedByGroupCode(group.getGroupCode(), true);
                userService.changeGroupCodeById(group.getLeader().getId(), group.getGroupCode());
                List<Group> groups = groupsService.getAllGroups();
                groups.removeIf(Group::isAccepted);
                groupEntryGrid.setItems(new ListDataProvider<>(groups));

                Message message = new Message();
                message.setUser(userService.findById(group.getLeader().getId()));
                message.setText(getTranslation("groups.accept.info"));

                messageService.saveMessage(message);
            });
            return button;
        })).setHeader(getTranslation("groups.accept"));
        groupEntryGrid.addColumn(new ComponentRenderer<>(e -> {
            Button button = new Button(getTranslation("groups.deny"));
            button.getStyle().set("color", "red");
            button.addClickListener(event -> {
                groupsService.deleteGroupByGroupCode(e.getGroupCode());
                List<Group> groups = groupsService.getAllGroups();
                groups.removeIf(Group::isAccepted);
                groupEntryGrid.setItems(new ListDataProvider<>(groups));
            });
            return button;
        })).setHeader(getTranslation("groups.deny"));

        List<Group> groups = groupsService.getAllGroups();
        groups.removeIf(Group::isAccepted);
        groupEntryGrid.setItems(groups);
        groupEntryGrid.setSizeFull();
        groupEntryGrid.getColumns().forEach(column -> column.setAutoWidth(true));
        setSizeFull();

        add(new AdminReturnButton(), groupEntryGrid);
    }

    @Override
    public String getPageTitle() {
        return getTranslation("page.confirm.groups");
    }
}
