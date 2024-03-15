package org.jk.eSked.ui.components.schedule;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import org.jk.eSked.backend.model.schedule.ScheduleEntry;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.backend.service.user.ScheduleService;
import org.jk.eSked.backend.service.user.UserService;
import org.jk.eSked.ui.components.scheduleDialogs.AddEntryDialog;
import org.jk.eSked.ui.components.scheduleDialogs.DeleteEntryDialog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class ScheduleGridNewEntries extends VerticalLayout {
    private final ScheduleService scheduleService;
    private final Grid<Button> scheduleGrid;
    private final List<Button> buttons = new ArrayList<>();
    private final UUID userId;
    private Collection<ScheduleEntry> entries;

    public ScheduleGridNewEntries(ScheduleService scheduleService, UserService userService) {
        this.scheduleService = scheduleService;
        this.userId = SessionService.getUserId();

        entries = scheduleService.getScheduleEntries(userId);

        scheduleGrid = new Schedule(userService, userId) {
            @Override
            Component rowRenderer(Button e, int day) {
                Button button = new Button(getTranslation("add"));
                button.setSizeFull();
                for (ScheduleEntry entry : entries) {
                    if (entry.getHour() == Integer.parseInt(e.getText()) && entry.getDay() == day) {
                        button.setText(entry.getSubject());
                        button.addClickListener(clickEvent -> {
                            DeleteEntryDialog dialog = new DeleteEntryDialog(scheduleService, entry);
                            dialog.addDetachListener(action -> refresh());
                            dialog.open();
                        });
                        button.getStyle().set("color", "green");
                        return button;
                    }
                }
                button.addClickListener(clickEvent -> {
                    AddEntryDialog dialog = new AddEntryDialog(scheduleService, Integer.parseInt(e.getText()), day, false);
                    dialog.addDetachListener(action -> refresh());
                    dialog.open();
                });
                button.getStyle().set("color", "red");
                return button;
            }

            @Override
            Component hourRenderer(Button e) {
                return new Text(Integer.toString(Integer.parseInt(e.getText()) + 1));
            }
        };
        scheduleGrid.setAllRowsVisible(true);

        Button more = new Button(new Icon(VaadinIcon.ARROW_DOWN), event -> ScheduleGrid.addRow(buttons, scheduleGrid));
        more.setWidth("100%");

        for (int i = 0; i < getMaxHour(); i++) ScheduleGrid.addRow(buttons, scheduleGrid);
        if (SessionService.isSessionMobile()) {
            setMobileColumns(1);
            AtomicInteger triggeredColumn = new AtomicInteger(1);
            Button next = new Button(VaadinIcon.ARROW_RIGHT.create(), nextColumn -> {
                if (triggeredColumn.get() != 5) triggeredColumn.set(triggeredColumn.get() + 1);
                setMobileColumns(triggeredColumn.get());
            });
            next.setWidth("100%");
            Button prev = new Button(VaadinIcon.ARROW_LEFT.create(), prevColumn -> {
                if (triggeredColumn.get() != 1) triggeredColumn.set(triggeredColumn.get() - 1);
                setMobileColumns(triggeredColumn.get());
            });
            prev.setWidth("100%");
            HorizontalLayout layout = new HorizontalLayout(prev, next);
            layout.setWidth("100%");
            add(scheduleGrid, layout);
        } else add(scheduleGrid);
        setSizeFull();
        add(more);
    }

    private void setMobileColumns(int pos) {
        for (int i = 1; i < 6; i++)
            scheduleGrid.getColumnByKey(Integer.toString(i)).setVisible(i == pos);
    }

    private int getMaxHour() {
        int maxHour = 0;
        for (ScheduleEntry entry : entries) maxHour = Math.max(maxHour, entry.getHour());

        return maxHour + 1;
    }

    private void refresh() {
        entries = scheduleService.getScheduleEntries(userId);
        ListDataProvider<Button> dataProvider = new ListDataProvider<>(buttons);
        scheduleGrid.setItems(dataProvider);
    }
}
