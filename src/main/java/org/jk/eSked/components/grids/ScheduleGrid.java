package org.jk.eSked.components.grids;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.jk.eSked.model.ScheduleHour;
import org.jk.eSked.model.entry.ScheduleEntry;
import org.jk.eSked.model.event.Event;
import org.jk.eSked.model.event.EventType;
import org.jk.eSked.services.events.EventService;
import org.jk.eSked.services.hours.HoursService;
import org.jk.eSked.services.schedule.ScheduleService;
import org.jk.eSked.services.users.UserService;
import org.jk.eSked.view.schedule.AddNewEventDialog;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class ScheduleGrid extends VerticalLayout {
    private final ScheduleService scheduleService;
    private final EventService eventService;
    private final UserService userService;
    private static final boolean NEXT_WEEK = true;
    private static final boolean PREVIOUS_WEEK = false;
    private final DatePicker dateFrom;
    private final DatePicker dateTo;
    private LocalDate startOfWeek;
    private final Grid<Button> scheduleGrid;
    private final List<Button> buttons = new ArrayList<>();
    private Collection<ScheduleEntry> entries;
    private Collection<Event> events;
    private final UUID userID;

    public ScheduleGrid(ScheduleService scheduleService, EventService eventService, UserService userService, HoursService hoursService, UUID userId) {
        this.scheduleService = scheduleService;
        this.eventService = eventService;
        this.userService = userService;
        this.userID = userId;

        if (startOfWeek == null) {
            startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY);
        }

        entries = scheduleService.getScheduleEntries(userId);
        events = eventService.getEvents(startOfWeek, userId);

        Button prevWeek = new Button(new Icon(VaadinIcon.ARROW_LEFT));
        prevWeek.addClickListener(e -> changeWeek(NEXT_WEEK));

        dateFrom = new DatePicker();
        dateFrom.setI18n(new DatePicker.DatePickerI18n().setWeek("tydzień").setCalendar("kalendarz")
                .setClear("wyczyść").setToday("dzisiaj")
                .setCancel("zamknij").setFirstDayOfWeek(1)
                .setMonthNames(Arrays.asList("styczeń", "luty", "marzec", "kwiecień", "maj", "czerwiec", "lipiec", "sierpień", "wrzesień", "październik", "listopad", "grudzień"))
                .setWeekdays(Arrays.asList("niedziela", "poniedziałek", "wtorek", "środa", "czwartek", "piątek", "sobota"))
                .setWeekdaysShort(Arrays.asList("niedź", "pon", "wt", "śr", "czw", "pt", "sob")));
        dateFrom.addValueChangeListener(event -> setWeekForDay(event.getValue()));

        Icon arrowIcon = new Icon(VaadinIcon.ARROW_RIGHT);

        dateTo = new DatePicker();
        dateTo.setEnabled(false);

        Button nextWeek = new Button(new Icon(VaadinIcon.ARROW_RIGHT));
        nextWeek.addClickListener(f -> changeWeek(PREVIOUS_WEEK));

        HorizontalLayout datePanel = new HorizontalLayout(prevWeek, dateFrom, arrowIcon, dateTo, nextWeek);
        datePanel.setWidth("100%");
        prevWeek.setWidth("20%");
        dateFrom.setWidth("25%");
        arrowIcon.getStyle().set("width", "10%");
        dateTo.setWidth("25%");
        nextWeek.setWidth("20%");
        datePanel.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        scheduleGrid = new Grid<>();
        if (userService.getScheduleHours(userId))
            scheduleGrid.addColumn(new ComponentRenderer<>(e -> {
                String text = Integer.toString(Integer.parseInt(e.getText()) + 1);
                ScheduleHour scheduleHour = hoursService.getScheduleHour(userId, Integer.parseInt(e.getText()) + 1);
                if (scheduleHour != null)
                    text = hoursService.getScheduleHour(userId, Integer.parseInt(e.getText()) + 1).getData();
                return new Label(text);
            })).setHeader("G|D").setAutoWidth(true).setFlexGrow(0);
        scheduleGrid.addColumn(new ComponentRenderer<>(e -> rowRenderer(e, 0))).setHeader("Poniedziałek");
        scheduleGrid.addColumn(new ComponentRenderer<>(e -> rowRenderer(e, 1))).setHeader("Wtorek");
        scheduleGrid.addColumn(new ComponentRenderer<>(e -> rowRenderer(e, 2))).setHeader("Środa");
        scheduleGrid.addColumn(new ComponentRenderer<>(e -> rowRenderer(e, 3))).setHeader("Czwartek");
        scheduleGrid.addColumn(new ComponentRenderer<>(e -> rowRenderer(e, 4))).setHeader("Piątek");
        scheduleGrid.setSelectionMode(Grid.SelectionMode.NONE);
        scheduleGrid.getColumns().forEach(column -> column.setTextAlign(ColumnTextAlign.CENTER));
        scheduleGrid.setHeightByRows(true);
        scheduleGrid.setVerticalScrollingEnabled(true);
        scheduleGrid.recalculateColumnWidths();

        for (int i = 0; i < getMaxHour(); i++) addRow();

        add(datePanel, scheduleGrid);
        refreshDates();
    }

    private Component rowRenderer(Button e, int day) {
        Button button = new Button("-");
        button.setSizeFull();
        for (ScheduleEntry entry : entries) {
            int hour = Integer.parseInt(e.getText());
            if (entry.getHour() == hour && entry.getDay() == day) {
                List<Event> entryEvents = new ArrayList<>();
                String color;
                if (userService.getDarkTheme(userID)) {
                    button.getStyle().set("color", "white");
                    color = "#2c3d52";
                } else {
                    button.getStyle().set("color", "#1676f3");
                    color = "#f3f5f7";
                }
                for (Event event : events) {
                    if (event.getHour() == hour && event.getDate().getDayOfWeek() == DayOfWeek.of(day + 1)) {
                        entryEvents.add(event);
                        if (event.getEventType() == EventType.TEST) {
                            color = "#c43737";
                        } else {
                            if (event.getEventType() == EventType.QUIZ) {
                                color = "#e88133";
                            } else {
                                if (event.getEventType() == EventType.QUESTIONS) {
                                    color = "#ebbf23";
                                } else {
                                    if (event.getEventType() == EventType.HOMEWORK) {
                                        color = "#46c768";
                                    }
                                }
                            }
                        }
                    }
                }
                String subject = entry.getSubject();
                button.addClickListener(event -> addNewEvent(new ScheduleEntry(userID, hour, day, subject, Instant.now().toEpochMilli())));
                button.setText(subject + "(" + entryEvents.size() + ")");
                button.getStyle().set("background-color", color);
                return button;
            }
        }
        return button;
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

    private void setWeekForDay(LocalDate day) {
        day = (LocalDate) TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY).adjustInto(day);
        startOfWeek = day;
        refreshDates();
        refresh();
    }

    private void refreshDates() {
        dateFrom.setValue(startOfWeek);
        dateTo.setValue(startOfWeek.plusDays(6));
    }

    private void changeWeek(boolean weekType) {
        int deltaDays = (weekType == NEXT_WEEK) ? -7 : 7;
        startOfWeek = startOfWeek.plusDays(deltaDays).with(DayOfWeek.MONDAY);
        refreshDates();
        refresh();
    }

    private void addNewEvent(ScheduleEntry scheduleEntry) {
        AddNewEventDialog dialog = new AddNewEventDialog(scheduleService, eventService, startOfWeek, scheduleEntry, userID);
        dialog.setRefreshAction(this::refresh);
        dialog.open();
    }

    private void refresh() {
        entries = scheduleService.getScheduleEntries(userID);
        events = eventService.getEvents(startOfWeek, userID);
        ListDataProvider<Button> dataProvider = new ListDataProvider<>(buttons);
        scheduleGrid.setDataProvider(dataProvider);
    }
}