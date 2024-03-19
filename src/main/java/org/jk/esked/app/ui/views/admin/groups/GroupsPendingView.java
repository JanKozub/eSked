package org.jk.esked.app.ui.views.admin.groups;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.jk.esked.app.backend.model.Group;
import org.jk.esked.app.backend.model.Message;
import org.jk.esked.app.backend.security.SecurityService;
import org.jk.esked.app.backend.services.GroupService;
import org.jk.esked.app.backend.services.MessageService;
import org.jk.esked.app.backend.services.ScheduleService;
import org.jk.esked.app.backend.services.UserService;
import org.jk.esked.app.ui.components.AdminReturnButton;
import org.jk.esked.app.ui.components.admin.GroupsGrid;
import org.jk.esked.app.ui.views.MainLayout;

import java.util.List;

@Route(value = "admin/groups/pending", layout = MainLayout.class)
@RolesAllowed("ADMIN")
class GroupsPendingView extends VerticalLayout implements HasDynamicTitle {
    public GroupsPendingView(SecurityService securityService, ScheduleService scheduleService, GroupService groupsService, UserService userService, MessageService messageService) {
        GroupsGrid groupEntryGrid = new GroupsGrid(securityService.getUser(), scheduleService, userService);

        groupEntryGrid.addColumn(new ComponentRenderer<>(group -> {
            Button button = new Button(getTranslation("groups.accept"));
            button.getStyle().set("color", "green");
            button.addClickListener(event -> {
                groupsService.changeGroupAcceptedByGroupCode(group.getGroupCode(), true);
                userService.changeGroupCodeByUserId(group.getLeader().getId(), group.getGroupCode());
                List<Group> groups = groupsService.getAllGroups();
                groups.removeIf(Group::isAccepted);
                groupEntryGrid.setItems(new ListDataProvider<>(groups));

                Message message = new Message();
                message.setUser(userService.getUserById(group.getLeader().getId()));
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
