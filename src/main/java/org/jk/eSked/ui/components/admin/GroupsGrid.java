package org.jk.eSked.ui.components.admin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.backend.model.Group;
import org.jk.eSked.backend.service.user.ScheduleService;
import org.jk.eSked.backend.service.user.UserService;
import org.jk.eSked.ui.components.schedule.ScheduleGridNewEntries;

import java.util.Locale;

public class GroupsGrid extends Grid<Group> {
    private final static Locale locale = VaadinSession.getCurrent().getLocale();
    private final ScheduleService scheduleService;
    private final UserService userService;

    public GroupsGrid(ScheduleService scheduleService, UserService userService) {
        this.scheduleService = scheduleService;
        this.userService = userService;

        Dialog dialog = groupLayout();

        addColumn(Group::getName).setHeader(getTranslation(locale, "title"));
        addColumn(Group::getGroupCode).setHeader(getTranslation(locale, "groups_code"));
        addColumn(new ComponentRenderer<>(e -> {
            Button button = new Button(getTranslation(locale, "groups_details"));
            button.addClickListener(event -> dialog.open());
            return button;
        })).setHeader(getTranslation(locale, "groups_details"));
    }

    private Dialog groupLayout() {
        Dialog dialog = new Dialog();

        Button button = new Button(getTranslation(locale, "return"), event -> dialog.close());
        button.setWidth("100%");
        ScheduleGridNewEntries scheduleGridNewEntries = new ScheduleGridNewEntries(scheduleService, userService);

        dialog.getHeader().add(button);
        dialog.add(scheduleGridNewEntries);
        dialog.setSizeFull();

        return dialog;
    }
}
