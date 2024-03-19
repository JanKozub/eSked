package org.jk.esked.app.ui.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.jk.esked.app.backend.services.UserService;

import java.util.UUID;

abstract class Schedule extends Grid<Button> {
    Schedule(UserService userService, UUID userId) {
        if (userService.isHourEnabled(userId))
            addColumn(new ComponentRenderer<>(this::hourRenderer))
                    .setHeader(getTranslation("hour.s")).setAutoWidth(true).setFlexGrow(0);

        for (int i = 0; i < 5; i++) {
            int finalI = i;
            addColumn(new ComponentRenderer<>(e -> rowRenderer(e, finalI))).setHeader(getTranslation("day." + (i + 1))).setKey(String.valueOf(i + 1));
        }

        getColumns().forEach(column -> column.setTextAlign(ColumnTextAlign.CENTER));
        setSelectionMode(SelectionMode.NONE);
        recalculateColumnWidths();
        setHeight("60%");
    }

    abstract Component rowRenderer(Button e, int day);

    abstract Component hourRenderer(Button e);
}
