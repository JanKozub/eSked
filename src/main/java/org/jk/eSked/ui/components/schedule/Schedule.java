package org.jk.eSked.ui.components.schedule;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.backend.service.user.UserService;

import java.util.Locale;
import java.util.UUID;

abstract class Schedule extends Grid<Button> {
    private final static Locale locale = VaadinSession.getCurrent().getLocale();

    Schedule(UserService userService, UUID userId) {
        if (userService.getScheduleHours(userId))
            addColumn(new ComponentRenderer<>(this::hourRenderer))
                    .setHeader(getTranslation(locale, "hour_s")).setAutoWidth(true).setFlexGrow(0);

        for (int i = 0; i < 5; i++) {
            int finalI = i;
            addColumn(new ComponentRenderer<>(e -> rowRenderer(e, finalI))).setHeader(getTranslation(locale, "day_" + (i + 1))).setKey(String.valueOf(i + 1));
        }

        getColumns().forEach(column -> column.setTextAlign(ColumnTextAlign.CENTER));
        setSelectionMode(Grid.SelectionMode.NONE);
        setVerticalScrollingEnabled(true);
        recalculateColumnWidths();
        setHeight("60%");
    }

    abstract Component rowRenderer(Button e, int day);

    abstract Component hourRenderer(Button e);
}
