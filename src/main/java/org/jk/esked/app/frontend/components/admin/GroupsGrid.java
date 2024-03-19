package org.jk.esked.app.frontend.components.admin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.jk.esked.app.backend.model.Group;
import org.jk.esked.app.backend.model.User;
import org.jk.esked.app.backend.services.ScheduleService;
import org.jk.esked.app.backend.services.UserService;
import org.jk.esked.app.frontend.components.schedule.ScheduleGridNewEntries;

public class GroupsGrid extends Grid<Group> {
    private final ScheduleService scheduleService;
    private final UserService userService;
    private final User user;

    public GroupsGrid(User user, ScheduleService scheduleService, UserService userService) {
        this.scheduleService = scheduleService;
        this.userService = userService;
        this.user = user;

        Dialog dialog = groupLayout();

        addColumn(Group::getName).setHeader(getTranslation("title"));
        addColumn(Group::getGroupCode).setHeader(getTranslation("groups.code"));
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
        ScheduleGridNewEntries scheduleGridNewEntries = new ScheduleGridNewEntries(user, scheduleService, userService);

        dialog.getHeader().add(button);
        dialog.add(scheduleGridNewEntries);
        dialog.setSizeFull();

        return dialog;
    }
}
