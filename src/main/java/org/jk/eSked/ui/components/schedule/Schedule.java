package org.jk.eSked.ui.components.schedule;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.jk.eSked.backend.service.UserService;

import java.util.UUID;

abstract class Schedule extends Grid<Button> {
    Schedule(UserService userService, UUID userId) {
        if (userService.getScheduleHours(userId))
            addColumn(new ComponentRenderer<>(this::hourRenderer)).setHeader("G|D").setAutoWidth(true).setFlexGrow(0);
        addColumn(new ComponentRenderer<>(e -> rowRenderer(e, 0))).setHeader("Poniedziałek").setKey("1");
        addColumn(new ComponentRenderer<>(e -> rowRenderer(e, 1))).setHeader("Wtorek").setKey("2");
        addColumn(new ComponentRenderer<>(e -> rowRenderer(e, 2))).setHeader("Środa").setKey("3");
        addColumn(new ComponentRenderer<>(e -> rowRenderer(e, 3))).setHeader("Czwartek").setKey("4");
        addColumn(new ComponentRenderer<>(e -> rowRenderer(e, 4))).setHeader("Piątek").setKey("5");
        getColumns().forEach(column -> column.setTextAlign(ColumnTextAlign.CENTER));
        setSelectionMode(Grid.SelectionMode.NONE);
        setHeightByRows(true);
        setVerticalScrollingEnabled(true);
        recalculateColumnWidths();
        setHeight("60%");
    }

    abstract Component rowRenderer(Button e, int day);

    abstract Component hourRenderer(Button e);
}