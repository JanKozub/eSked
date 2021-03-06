package org.jk.eSked.ui.components.schedule;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.BasicRenderer;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import org.jk.eSked.backend.model.Event;
import org.jk.eSked.backend.model.schedule.ScheduleEntry;
import org.jk.eSked.backend.service.TimeService;
import org.jk.eSked.backend.service.user.EventService;
import org.jk.eSked.backend.service.user.ScheduleService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

public class EventGrid extends VerticalLayout {
    private static final boolean NEXT_WEEK = true;
    private static final boolean PREVIOUS_WEEK = false;
    private final EventService eventService;
    private final UUID userId;
    private final DatePicker dateFrom;
    private final DatePicker dateTo;
    private final Grid<Event> eventGrid;
    private final Collection<ScheduleEntry> entries;
    private LocalDate startOfWeek;

    public EventGrid(ScheduleService scheduleService, EventService eventService, UUID userId) {
        this.eventService = eventService;
        this.userId = userId;

        if (startOfWeek == null) {
            startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY);
        }

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

        eventGrid = new Grid<>();
        eventGrid.setHeightByRows(true);

        entries = scheduleService.getScheduleEntries(userId);

        eventGrid.addColumn(event -> event.getType().getDescription()).setHeader("Rodzaj");
        eventGrid.addColumn(new BasicRenderer<>(event -> {
            if (entries != null) {
                for (ScheduleEntry entry : entries) {
                    if (entry.getHour() == event.getHour() && entry.getDay() == TimeService.InstantToLocalDate(event.getTimestamp()).getDayOfWeek().getValue() - 1)
                        return entry.getSubject() + "(" + entry.getHour() + ")";
                }
            }
            return "brak";
        }) {
        }).setHeader("Lekcja(Godz)");
        eventGrid.addColumn(new LocalDateTimeRenderer<>(event ->
                TimeService.InstantToLocalDateTime(event.getTimestamp()),
                DateTimeFormatter.ofPattern("EEEE"))).setHeader("Dzień");
        eventGrid.addColumn(event -> TimeService.InstantToLocalDate(event.getTimestamp())).setHeader("Data");
        eventGrid.addColumn(Event::getTopic).setHeader("Temat");
        eventGrid.addColumn(new ComponentRenderer<>(e -> {
            Icon icon = new Icon(VaadinIcon.TRASH);
            icon.getStyle().set("cursor", "pointer");
            icon.addClickListener(event -> {
                eventService.deleteEvent(userId, e.getEventId());
                reload();
            });
            return icon;
        })).setHeader("Usuń");
        eventGrid.setSelectionMode(Grid.SelectionMode.NONE);
        eventGrid.setWidth("100%");
        eventGrid.getColumns().forEach(column -> column.setAutoWidth(true));

        refreshDates();
        add(datePanel, eventGrid);
    }

    private void setWeekForDay(LocalDate day) {
        day = (LocalDate) TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY).adjustInto(day);
        startOfWeek = day;
        refreshDates();
        reload();
    }

    private void changeWeek(boolean weekType) {
        int deltaDays = (weekType == NEXT_WEEK) ? -7 : 7;
        startOfWeek = startOfWeek.plusDays(deltaDays).with(DayOfWeek.MONDAY);
        refreshDates();
        reload();
    }

    private void refreshDates() {
        if (startOfWeek != null) {
            dateFrom.setValue(startOfWeek);
            dateTo.setValue(startOfWeek.plusDays(6));
        }
    }

    private void reload() {
        Collection<Event> events = eventService.getEventsForWeek(startOfWeek, userId);
        events.stream().filter(event -> !event.isCheckedFlag())
                .forEach(event -> eventService.setCheckedFlag(event.getEventId(), userId, true));
        eventGrid.setDataProvider(new ListDataProvider<>(events));
    }
}
