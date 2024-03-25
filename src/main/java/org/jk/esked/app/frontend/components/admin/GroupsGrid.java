package org.jk.esked.app.frontend.components.admin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.jk.esked.app.backend.model.entities.Group;
import org.jk.esked.app.backend.model.entities.User;
import org.jk.esked.app.backend.services.HourService;
import org.jk.esked.app.backend.services.ScheduleEntryService;
import org.jk.esked.app.backend.services.UserService;
import org.jk.esked.app.backend.services.utilities.TimeService;
import org.jk.esked.app.frontend.components.buttons.WideButton;
import org.jk.esked.app.frontend.components.schedule.ScheduleGridNewEntries;

public class GroupsGrid extends Grid<Group> {
    public GroupsGrid(User user, ScheduleEntryService scheduleEntryService, UserService userService, HourService hourService) {
        Dialog dialog = groupLayout(user, userService, hourService, scheduleEntryService);

        addColumn(Group::getGroupCode).setHeader(getTranslation("groups.code"));
        addColumn(group -> TimeService.timestampToFormatedString(group.getCreatedDate())).setHeader(getTranslation("date.created"));
        addColumn(new ComponentRenderer<>(e -> new Button(getTranslation("groups.details"), event -> dialog.open()))).setHeader(getTranslation("groups.details"));
    }

    private Dialog groupLayout(User user, UserService userService, HourService hourService, ScheduleEntryService scheduleEntryService) {
        Dialog dialog = new Dialog();
        dialog.getHeader().add(new WideButton(getTranslation("return"), event -> dialog.close()));
        dialog.add(new ScheduleGridNewEntries(user, scheduleEntryService, userService, hourService));
        dialog.setSizeFull();

        return dialog;
    }
}
