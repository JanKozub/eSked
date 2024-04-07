package org.jk.esked.app.frontend.components.schedule;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import org.jk.esked.app.backend.model.entities.Event;
import org.jk.esked.app.backend.model.entities.Hour;
import org.jk.esked.app.backend.model.entities.ScheduleEntry;
import org.jk.esked.app.backend.model.entities.User;
import org.jk.esked.app.backend.services.EventService;
import org.jk.esked.app.backend.services.HourService;
import org.jk.esked.app.backend.services.ScheduleEntryService;
import org.jk.esked.app.backend.services.UserService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

public class ScheduleGrid extends VerticalLayout {
    private final ScheduleEntryService scheduleEntryService;
    private final EventService eventService;
    private final HourService hourService;
    private final User user;
    private final Grid<Button> scheduleGrid;
    private final List<Button> buttons = new ArrayList<>();
    private LocalDate startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY);

    public ScheduleGrid(User user, ScheduleEntryService scheduleEntryService, EventService eventService, UserService userService, HourService hoursService) {
        this.scheduleEntryService = scheduleEntryService;
        this.eventService = eventService;
        this.hourService = hoursService;
        this.user = user;

        scheduleGrid = new Schedule(userService, user.getId()) {
            @Override
            Component rowRenderer(Button e, int day) {
                return ScheduleGrid.this.rowRenderer(e, day);
            }

            @Override
            Component hourRenderer(Button e) {
                return ScheduleGrid.this.hourRenderer(e);
            }
        };

        for (int i = 0; i < hoursService.getScheduleMaxHour(user.getId()); i++) addRow(buttons, scheduleGrid);

        HorizontalLayout datePanel = new DatePanel(startOfWeek) {
            @Override
            public void setWeekForDay(LocalDate day) {
                day = (LocalDate) TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY).adjustInto(day);
                startOfWeek = day;
                refresh();
            }
        };

        addClassName("schedule-grid");
        add(datePanel, scheduleGrid);
    }

    public static void addRow(List<Button> buttons, Grid<Button> scheduleGrid) {
        int size = buttons.size();
        if (size > 0) {
            int maxNum = Integer.parseInt(buttons.get(size - 1).getText()) + 1;
            buttons.add(new Button(Integer.toString(maxNum)));
        } else buttons.add(new Button("0"));
        scheduleGrid.setItems(buttons);
    }

    private void refresh() {
        scheduleGrid.setItems(new ListDataProvider<>(buttons));
    }

    private Component rowRenderer(Button e, int day) {
        int hour = Integer.parseInt(e.getText());
        Button button = new Button("-");
        ScheduleEntry entry = scheduleEntryService.findByUserIdAndDayAndHour(user.getId(), day, hour);

        if (entry == null) return button;

        List<Event> entryEvents = eventService.findByUserIdAndHourAndDay(user.getId(), hour, startOfWeek.plusDays(day));
        if (!entryEvents.isEmpty()) {
            Event maxEvent = entryEvents.get(0);

            for (Event event : entryEvents) {
                if (event.getEventType().getId() > maxEvent.getEventType().getId())
                    maxEvent = event;
            }

            button.getStyle().set("background-color", maxEvent.getEventType().getColor());
            button.getStyle().set("color", "white");
        }

        String subject = entry.getSubject();
        button.addClickListener(event -> addNewEvent(new ScheduleEntry(user, hour, day, subject)));
        button.setText(subject + "(" + entryEvents.size() + ")");

        return button;
    }

    private void addNewEvent(ScheduleEntry scheduleEntry) {
        AddNewEventDialog dialog = new AddNewEventDialog(user, scheduleEntryService, eventService, startOfWeek, scheduleEntry);
        dialog.addDialogCloseActionListener(close -> {
            refresh();
            dialog.close();
        });
        dialog.setRefreshAction(this::refresh);
        dialog.open();
    }

    private Component hourRenderer(Button e) {
        String text = Integer.toString(Integer.parseInt(e.getText()) + 1);
        Hour hour = hourService.getHourValueByHour(user.getId(), Integer.parseInt(e.getText()) + 1);
        if (hour != null)
            text = hourService.getHourValueByHour(user.getId(), Integer.parseInt(e.getText()) + 1).getData();
        return new Text(text);
    }
}
