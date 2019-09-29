package org.jk.eSked.ui.components.schedule;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.backend.model.ScheduleEntry;
import org.jk.eSked.backend.model.ScheduleHour;
import org.jk.eSked.backend.model.event.Event;
import org.jk.eSked.backend.service.EventService;
import org.jk.eSked.backend.service.HoursService;
import org.jk.eSked.backend.service.ScheduleService;
import org.jk.eSked.backend.service.UserService;
import org.jk.eSked.ui.views.schedule.AddNewEventDialog;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class ScheduleGrid extends VerticalLayout {
    private final ScheduleService scheduleService;
    private final EventService eventService;
    private final Grid<Button> scheduleGrid;
    private final List<Button> buttons = new ArrayList<>();
    private final UUID userId;
    private LocalDate startOfWeek;
    private Collection<ScheduleEntry> entries;
    private Collection<Event> events;

    public ScheduleGrid(ScheduleService scheduleService, EventService eventService, UserService userService, HoursService hoursService, UUID userId) {
        this.scheduleService = scheduleService;
        this.eventService = eventService;
        this.userId = userId;

        if (startOfWeek == null) startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY);

        entries = scheduleService.getScheduleEntries(userId);
        events = eventService.getEvents(startOfWeek, userId);

        scheduleGrid = new Schedule(userService, userId) {
            @Override
            Component rowRenderer(Button e, int day) {
                Button button = new Button("-");
                button.setSizeFull();
                for (ScheduleEntry entry : entries) {
                    int hour = Integer.parseInt(e.getText());
                    if (entry.getHour() == hour && entry.getDay() == day) {
                        List<Event> entryEvents = new ArrayList<>();
                        String color;
                        if (userService.getDarkTheme(ScheduleGrid.this.userId)) {
                            button.getStyle().set("color", "white");
                            color = "#2c3d52";
                        } else {
                            button.getStyle().set("color", "#1676f3");
                            color = "#f3f5f7";
                        }
                        for (Event event : events) {
                            if (event.getHour() == hour && event.getDate().getDayOfWeek() == DayOfWeek.of(day + 1)) {
                                entryEvents.add(event);
                                switch (event.getEventType()) {
                                    case TEST:
                                        color = "#c43737";
                                        break;
                                    case QUIZ:
                                        color = "#e88133";
                                        break;
                                    case QUESTIONS:
                                        color = "#ebbf23";
                                        break;
                                    case HOMEWORK:
                                        color = "#46c768";
                                        break;
                                }
                            }
                        }
                        String subject = entry.getSubject();
                        button.addClickListener(event -> addNewEvent(new ScheduleEntry(ScheduleGrid.this.userId, hour, day, subject, Instant.now().toEpochMilli())));
                        button.setText(subject + "(" + entryEvents.size() + ")");
                        button.getStyle().set("background-color", color);
                        return button;
                    }
                }
                return button;
            }

            @Override
            Component hourRenderer(Button e) {
                String text = Integer.toString(Integer.parseInt(e.getText()) + 1);
                ScheduleHour scheduleHour = hoursService.getScheduleHour(userId, Integer.parseInt(e.getText()) + 1);
                if (scheduleHour != null)
                    text = hoursService.getScheduleHour(userId, Integer.parseInt(e.getText()) + 1).getData();
                return new Label(text);
            }
        };

        for (int i = 0; i < getMaxHour(); i++) addRow();

        HorizontalLayout datePanel = new DatePanel(startOfWeek) {
            @Override
            void setWeekForDay(LocalDate day) {
                day = (LocalDate) TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY).adjustInto(day);
                startOfWeek = day;
                refresh();
            }

            @Override
            LocalDate changeWeek(boolean weekType) {
                int deltaDays = (weekType) ? -7 : 7;
                startOfWeek = startOfWeek.plusDays(deltaDays).with(DayOfWeek.MONDAY);
                refresh();
                return startOfWeek;
            }
        };
        add(datePanel);

        if (VaadinSession.getCurrent().getBrowser().getBrowserApplication().contains("Mobile")) {
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

    private void addRow() {
        int size = buttons.size();
        if (size > 0) {
            int maxNum = Integer.parseInt(buttons.get(size - 1).getText()) + 1;
            buttons.add(new Button(Integer.toString(maxNum)));
        } else buttons.add(new Button("0"));
        scheduleGrid.setItems(buttons);
    }

    private void addNewEvent(ScheduleEntry scheduleEntry) {
        AddNewEventDialog dialog = new AddNewEventDialog(scheduleService, eventService, startOfWeek, scheduleEntry, userId);
        dialog.addDialogCloseActionListener(close -> {
            refresh();
            dialog.close();
        });
        dialog.setRefreshAction(this::refresh);
        dialog.open();
    }

    private void refresh() {
        entries = scheduleService.getScheduleEntries(userId);
        events = eventService.getEvents(startOfWeek, userId);
        ListDataProvider<Button> dataProvider = new ListDataProvider<>(buttons);
        scheduleGrid.setDataProvider(dataProvider);
    }
}