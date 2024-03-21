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
import org.jk.esked.app.backend.model.types.ThemeType;
import org.jk.esked.app.backend.services.*;
import org.jk.esked.app.backend.services.utilities.TimeService;

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
    private List<ScheduleEntry> entries;
    private List<Event> events;

    public ScheduleGrid(User user, ScheduleEntryService scheduleEntryService, EventService eventService, UserService userService, HourService hoursService) {
        this.scheduleEntryService = scheduleEntryService;
        this.eventService = eventService;
        this.hourService = hoursService;
        this.user = user;

        entries = scheduleEntryService.getAllByUserId(user.getId());
        events = eventService.getEventsForWeek(user.getId(), startOfWeek);

        scheduleGrid = new Schedule(userService, user.getId()) {
            @Override
            Component rowRenderer(Button e, int day) {
                return ScheduleGrid.this.rowRenderer(userService, e, day);
            }

            @Override
            Component hourRenderer(Button e) {
                return ScheduleGrid.this.hourRenderer(e);
            }
        };
        scheduleGrid.setAllRowsVisible(true);

        for (int i = 0; i < getMaxHour(); i++) addRow(buttons, scheduleGrid);

        HorizontalLayout datePanel = new DatePanel(startOfWeek) {
            @Override
            public void setWeekForDay(LocalDate day) {
                day = (LocalDate) TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY).adjustInto(day);
                startOfWeek = day;
                refresh();
            }
        };

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

    private int getMaxHour() {
        int maxHour = 0;
        for (ScheduleEntry entry : entries) {
            if (entry.getHour() > maxHour) maxHour = entry.getHour();
        }
        return maxHour + 1;
    }

    private void refresh() {
        entries = scheduleEntryService.getAllByUserId(user.getId());
        events = eventService.getEventsForWeek(user.getId(), startOfWeek);
        ListDataProvider<Button> dataProvider = new ListDataProvider<>(buttons);
        scheduleGrid.setItems(dataProvider);
    }

    private Component rowRenderer(UserService userService, Button e, int day) {
        Button button = new Button("-");
        button.setSizeFull();
        for (ScheduleEntry entry : entries) {
            int hour = Integer.parseInt(e.getText());
            if (entry.getHour() == hour && entry.getDay() == day) {
                List<Event> entryEvents = new ArrayList<>();
                String color;
                if (userService.getThemeByUserId(ScheduleGrid.this.user.getId()) == ThemeType.DARK) {
                    button.getStyle().set("color", "white");
                    color = "#2c3d52";
                } else {
                    button.getStyle().set("color", "#1676f3");
                    color = "#f3f5f7";
                }
                for (Event event : events) {
                    if (event.getHour() == hour && TimeService.timestampToLocalDateTime(event.getTimestamp()).getDayOfWeek() == DayOfWeek.of(day + 1)) {
                        entryEvents.add(event);
                        color = switch (event.getEventType()) {
                            case TEST -> "#c43737";
                            case QUIZ -> "#e88133";
                            case QUESTIONS -> "#ebbf23";
                            case HOMEWORK -> "#46c768";
                        };
                    }
                }
                String subject = entry.getSubject();
                button.addClickListener(event -> {
                    ScheduleEntry scheduleEntry = new ScheduleEntry();
                    scheduleEntry.setUser(user);
                    scheduleEntry.setHour(hour);
                    scheduleEntry.setDay(day);
                    scheduleEntry.setSubject(subject);
                    addNewEvent(scheduleEntry);
                });
                button.setText(subject + "(" + entryEvents.size() + ")");
                button.getStyle().set("background-color", color);
                return button;
            }
        }
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
