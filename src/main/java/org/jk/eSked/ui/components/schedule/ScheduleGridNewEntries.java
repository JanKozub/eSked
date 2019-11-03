package org.jk.eSked.ui.components.schedule;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import org.jk.eSked.backend.model.schedule.ScheduleEntry;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.backend.service.user.ScheduleService;
import org.jk.eSked.backend.service.user.UserService;
import org.jk.eSked.ui.components.myComponents.NewEntryDialog;

import java.time.DayOfWeek;
import java.time.LocalDate;
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
    private LocalDate startOfWeek;
    private Collection<ScheduleEntry> entries;

    public ScheduleGridNewEntries(ScheduleService scheduleService, UserService userService) {
        this.scheduleService = scheduleService;
        this.userId = SessionService.getUserId();

        entries = scheduleService.getScheduleEntries(userId);

        if (startOfWeek == null) startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY);

        scheduleGrid = new Schedule(userService, userId) {
            @Override
            Component rowRenderer(Button e, int day) {
                Button button = new Button("Dodaj");
                button.setSizeFull();
                for (ScheduleEntry entry : entries) {
                    if (entry.getHour() == Integer.parseInt(e.getText()) && entry.getDay() == day) {
                        button.setText(entry.getSubject());
                        button.addClickListener(clickEvent -> deleteEntryDialog(entry));
                        button.getStyle().set("color", "green");
                        return button;
                    }
                }
                button.addClickListener(clickEvent -> new NewEntryDialog(day, Integer.parseInt(e.getText()), scheduleService, SessionService.isSessionMobile()) {
                    @Override
                    public void refresh() {
                        ScheduleGridNewEntries.this.refresh();
                    }
                });
                button.getStyle().set("color", "red");
                return button;
            }

            @Override
            Component hourRenderer(Button e) {
                return new Label(Integer.toString(Integer.parseInt(e.getText()) + 1));
            }
        };

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
        for (int i = 1; i < 6; i++) {
            if (i == pos) scheduleGrid.getColumnByKey(Integer.toString(i)).setVisible(true);
            else scheduleGrid.getColumnByKey(Integer.toString(i)).setVisible(false);
        }
    }

    private int getMaxHour() {
        int maxHour = 0;
        for (ScheduleEntry entry : entries) {
            if (entry.getHour() > maxHour) maxHour = entry.getHour();
        }
        return maxHour + 1;
    }

    private void deleteEntryDialog(ScheduleEntry entry) {
        Dialog dialog = new Dialog();

        Label label = new Label("Aktualny przedmiot:");
        Label name = new Label(entry.getSubject());

        Button deleteButton = new Button("Usuń!", event -> {
            scheduleService.deleteScheduleEntry(userId, entry.getHour(), entry.getDay());

            dialog.close();
            refresh();
        });
        deleteButton.addClickShortcut(Key.ENTER);
        deleteButton.setWidth("100%");

        VerticalLayout layout = new VerticalLayout(label, name, deleteButton);
        layout.setAlignItems(Alignment.CENTER);

        dialog.add(layout);
        dialog.open();
    }

    private void refresh() {
        entries = scheduleService.getScheduleEntries(userId);
        ListDataProvider<Button> dataProvider = new ListDataProvider<>(buttons);
        scheduleGrid.setDataProvider(dataProvider);
    }
}
