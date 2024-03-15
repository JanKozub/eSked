package org.jk.eSked.ui.components.admin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.jk.eSked.backend.model.Group;
import org.jk.eSked.backend.service.user.ScheduleService;
import org.jk.eSked.backend.service.user.UserService;
import org.jk.eSked.ui.components.schedule.ScheduleGridNewEntries;

public class GroupsGrid extends Grid<Group> {
    private final ScheduleService scheduleService;
    private final UserService userService;

    public GroupsGrid(ScheduleService scheduleService, UserService userService) {
        this.scheduleService = scheduleService;
        this.userService = userService;

        Dialog dialog = groupLayout();

        addColumn(Group::name).setHeader(getTranslation("title"));
        addColumn(Group::groupCode).setHeader(getTranslation("groups.code"));
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
        ScheduleGridNewEntries scheduleGridNewEntries = new ScheduleGridNewEntries(scheduleService, userService);

        dialog.getHeader().add(button);
        dialog.add(scheduleGridNewEntries);
        dialog.setSizeFull();

        return dialog;
    }
}
