package org.jk.esked.app.frontend.views.admin.groups;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.jk.esked.app.backend.security.SecurityService;
import org.jk.esked.app.backend.services.GroupService;
import org.jk.esked.app.backend.services.HourService;
import org.jk.esked.app.backend.services.ScheduleEntryService;
import org.jk.esked.app.backend.services.UserService;
import org.jk.esked.app.frontend.components.admin.AdminReturnButton;
import org.jk.esked.app.frontend.components.admin.GroupsGrid;
import org.jk.esked.app.frontend.components.buttons.RedButton;
import org.jk.esked.app.frontend.views.MainLayout;

@Route(value = "admin/groups", layout = MainLayout.class)
@RolesAllowed("ADMIN")
@CssImport("./styles/admin-styles.css")
class GroupsView extends VerticalLayout implements HasDynamicTitle {
    public GroupsView(ScheduleEntryService scheduleEntryService, GroupService groupsService, UserService userService, SecurityService securityService, HourService hourService) {
        GroupsGrid groupEntryGrid = new GroupsGrid(securityService.getUser(), scheduleEntryService, userService, hourService);

        groupEntryGrid.addColumn(new ComponentRenderer<>(e ->
                new RedButton(getTranslation("delete"), event -> {
                    groupsService.deleteByGroupCode(e.getGroupCode());
                    userService.findAllUsersByGroupCode(e.getGroupCode())
                            .forEach(u -> userService.changeGroupCodeById(u.getId(), 0));

                    groupEntryGrid.setItems(groupsService.findAllGroupsByAccepted(true));
                }))).setHeader(getTranslation("delete"));
        groupEntryGrid.setItems(groupsService.findAllGroupsByAccepted(true));
        groupEntryGrid.getColumns().forEach(column -> column.setAutoWidth(true));

        addClassName("groups-view");
        add(new AdminReturnButton(), groupEntryGrid);
    }

    @Override
    public String getPageTitle() {
        return getTranslation("page.groups");
    }
}
