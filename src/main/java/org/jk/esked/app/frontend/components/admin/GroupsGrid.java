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
import org.jk.esked.app.frontend.components.schedule.ScheduleGridNewEntries;

public class GroupsGrid extends Grid<Group> { //TODO cleanup
    private final ScheduleEntryService scheduleEntryService;
    private final UserService userService;
    private final HourService hourService;
    private final User user;

    public GroupsGrid(User user, ScheduleEntryService scheduleEntryService, UserService userService, HourService hourService) {
        this.scheduleEntryService = scheduleEntryService;
        this.userService = userService;
        this.hourService = hourService;
        this.user = user;

        Dialog dialog = groupLayout();

        addColumn(Group::getGroupCode).setHeader(getTranslation("groups.code"));
        addColumn(group -> TimeService.timestampToFormatedString(group.getCreatedDate())).setHeader(getTranslation("date.created"));
        addColumn(new ComponentRenderer<>(e -> {
            Button button = new Button(getTranslation("groups.details"));
            button.addClickListener(event -> dialog.open());
            return button;
        })).setHeader(getTranslation("groups.details"));
    }

    private Dialog groupLayout() {
        Dialog dialog = new Dialog();

        Button button = new Button(getTranslation("return"), event -> dialog.close());
        button.setWidth("100%");
        ScheduleGridNewEntries scheduleGridNewEntries = new ScheduleGridNewEntries(user, scheduleEntryService, userService, hourService);

        dialog.getHeader().add(button);
        dialog.add(scheduleGridNewEntries);
        dialog.setSizeFull();

        return dialog;
    }
}
