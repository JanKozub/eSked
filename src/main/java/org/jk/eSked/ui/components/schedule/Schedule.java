package org.jk.eSked.ui.components.schedule;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.jk.eSked.backend.service.user.UserService;

import java.util.UUID;

abstract class Schedule extends Grid<Button> {
    Schedule(UserService userService, UUID userId) {
        if (userService.getScheduleHours(userId))
            addColumn(new ComponentRenderer<>(this::hourRenderer))
                    .setHeader(getTranslation("hour.s")).setAutoWidth(true).setFlexGrow(0);

        for (int i = 0; i < 5; i++) {
            int finalI = i;
            addColumn(new ComponentRenderer<>(e -> rowRenderer(e, finalI))).setHeader(getTranslation("day." + (i + 1))).setKey(String.valueOf(i + 1));
        }

        getColumns().forEach(column -> column.setTextAlign(ColumnTextAlign.CENTER));
        setSelectionMode(Grid.SelectionMode.NONE);
        recalculateColumnWidths();
        setHeight("60%");
    }

    abstract Component rowRenderer(Button e, int day);

    abstract Component hourRenderer(Button e);
}
