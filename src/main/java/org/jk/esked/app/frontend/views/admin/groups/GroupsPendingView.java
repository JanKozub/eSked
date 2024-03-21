package org.jk.esked.app.frontend.views.admin.groups;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.jk.esked.app.backend.model.entities.Message;
import org.jk.esked.app.backend.security.SecurityService;
import org.jk.esked.app.backend.services.*;
import org.jk.esked.app.frontend.components.admin.AdminReturnButton;
import org.jk.esked.app.frontend.components.admin.GroupsGrid;
import org.jk.esked.app.frontend.components.other.GreenButton;
import org.jk.esked.app.frontend.components.other.RedButton;
import org.jk.esked.app.frontend.views.MainLayout;

@Route(value = "admin/groups/pending", layout = MainLayout.class)
@RolesAllowed("ADMIN")
class GroupsPendingView extends VerticalLayout implements HasDynamicTitle {
    public GroupsPendingView(SecurityService securityService, ScheduleEntryService scheduleEntryService, GroupService groupsService, UserService userService, MessageService messageService, HourService hourService) {
        GroupsGrid groupEntryGrid = new GroupsGrid(securityService.getUser(), scheduleEntryService, userService, hourService);

        groupEntryGrid.addColumn(new ComponentRenderer<>(group ->
                new GreenButton(getTranslation("groups.accept"), event -> {
                    groupsService.changeAcceptedByGroupCode(group.getGroupCode(), true);
                    userService.changeGroupCodeById(group.getLeader().getId(), group.getGroupCode());
                    groupEntryGrid.setItems(new ListDataProvider<>(groupsService.findAllGroupsByAccepted(false)));

                    messageService.saveMessage(new Message(userService.findById(group.getLeader().getId()), getTranslation("groups.accept.info")));
                }))).setHeader(getTranslation("groups.accept"));
        groupEntryGrid.addColumn(new ComponentRenderer<>(e ->
                new RedButton(getTranslation("groups.deny"), event -> {
                    groupsService.deleteByGroupCode(e.getGroupCode());
                    groupEntryGrid.setItems(new ListDataProvider<>(groupsService.findAllGroupsByAccepted(false)));
                }))).setHeader(getTranslation("groups.deny"));

        groupEntryGrid.setItems(groupsService.findAllGroupsByAccepted(false));
        groupEntryGrid.getColumns().forEach(column -> column.setAutoWidth(true));
        setSizeFull();

        add(new AdminReturnButton(), groupEntryGrid);
    }

    @Override
    public String getPageTitle() {
        return getTranslation("page.confirm.groups");
    }
}
