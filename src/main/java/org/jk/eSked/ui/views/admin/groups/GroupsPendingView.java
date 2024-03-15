package org.jk.eSked.ui.views.admin.groups;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import org.jk.eSked.backend.model.Group;
import org.jk.eSked.backend.model.Message;
import org.jk.eSked.backend.service.user.GroupService;
import org.jk.eSked.backend.service.user.MessagesService;
import org.jk.eSked.backend.service.user.ScheduleService;
import org.jk.eSked.backend.service.user.UserService;
import org.jk.eSked.ui.components.admin.GroupsGrid;
import org.jk.eSked.ui.components.myComponents.AdminReturnButton;
import org.jk.eSked.ui.views.MainLayout;
import org.springframework.security.access.annotation.Secured;

import java.time.Instant;
import java.util.Collection;
@Route(value = "admin/groups/pending", layout = MainLayout.class)
@Secured("ROLE_ADMIN")
class GroupsPendingView extends VerticalLayout implements HasDynamicTitle {
    public GroupsPendingView(ScheduleService scheduleService, GroupService groupsService, UserService userService, MessagesService messagesService) {
        GroupsGrid groupEntryGrid = new GroupsGrid(scheduleService, userService);

        groupEntryGrid.addColumn(new ComponentRenderer<>(group -> {
            Button button = new Button(getTranslation("groups.accept"));
            button.getStyle().set("color", "green");
            button.addClickListener(event -> {
                groupsService.setGroupAccepted(group.groupCode());
                userService.setGroupCode(group.leaderId(), group.groupCode());
                Collection<Group> groups = groupsService.getGroups();
                groups.removeIf(Group::isAccepted);
                groupEntryGrid.setItems(new ListDataProvider<>(groups));

                messagesService.addMessageForUser(new Message(
                        group.leaderId(),
                        messagesService.generateMessageId(),
                        Instant.now().toEpochMilli(),
                        getTranslation("groups.accept.info"),
                        false
                ));
            });
            return button;
        })).setHeader(getTranslation("groups.accept"));
        groupEntryGrid.addColumn(new ComponentRenderer<>(e -> {
            Button button = new Button(getTranslation("groups.deny"));
            button.getStyle().set("color", "red");
            button.addClickListener(event -> {
                groupsService.deleteGroup(e.groupCode());
                Collection<Group> groups = groupsService.getGroups();
                groups.removeIf(Group::isAccepted);
                groupEntryGrid.setItems(new ListDataProvider<>(groups));
            });
            return button;
        })).setHeader(getTranslation("groups.deny"));

        Collection<Group> groups = groupsService.getGroups();
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
