package org.jk.eSked.ui.components.events;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.renderer.BasicRenderer;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.backend.model.Event;
import org.jk.eSked.backend.model.schedule.ScheduleEntry;
import org.jk.eSked.backend.service.SessionService;
import org.jk.eSked.backend.service.TimeService;
import org.jk.eSked.backend.service.user.EventService;
import org.jk.eSked.backend.service.user.ScheduleService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class EventGrid extends Grid<Event> {
    private final static Locale locale = VaadinSession.getCurrent().getLocale();
    private final EventService eventService;
    private final UUID userId;

    public EventGrid(ScheduleService  scheduleService, EventService eventService, LocalDate startOfWeek) {
        this.eventService = eventService;
        this.userId = SessionService.getUserId();

        Collection<ScheduleEntry> entries = scheduleService.getScheduleEntries(userId);

        setSelectionMode(Grid.SelectionMode.NONE);
        setWidth("100%");
        getColumns().forEach(column -> column.setAutoWidth(true));
        setAllRowsVisible(true);

        addColumn(event -> getTranslation(locale, event.getType().getDescription())).setHeader(getTranslation(locale, "type"));
        addColumn(new BasicRenderer<>(event -> {
            if (entries == null) return getTranslation(locale, "no.entries");

            for (ScheduleEntry entry : entries) {
                if (entry.getHour() == event.getHour() && entry.getDay() == TimeService.InstantToLocalDate(event.getTimestamp()).getDayOfWeek().getValue() - 1)
                    return entry.getSubject() + "(" + entry.getHour() + ")";
            }

            return getTranslation(locale, "no.entries");
        }) {
        }).setHeader(getTranslation(locale, "events.hour.header"));

        addColumn(new LocalDateTimeRenderer<>(event ->
                TimeService.InstantToLocalDateTime(event.getTimestamp()),
                () -> DateTimeFormatter.ofPattern("EEEE"))).setHeader(getTranslation(locale, "day"));
        addColumn(event -> TimeService.InstantToLocalDate(event.getTimestamp())).setHeader(getTranslation(locale, "date"));
        addColumn(Event::getTopic).setHeader(getTranslation(locale, "topic"));
        addColumn(new ComponentRenderer<>(e -> {
            Icon icon = new Icon(VaadinIcon.TRASH);
            icon.getStyle().set("cursor", "pointer");
            icon.addClickListener(event -> {
                eventService.deleteEvent(userId, e.getEventId());
                reloadForWeek(startOfWeek);
            });
            return icon;
        })).setHeader(getTranslation(locale, "delete"));
    }

    public void reloadForWeek(LocalDate startOfWeek) {
        Collection<Event> events = eventService.getEventsForWeek(startOfWeek, userId);
        events.stream().filter(event -> !event.isCheckedFlag())
                .forEach(event -> eventService.setCheckedFlag(event.getEventId(), userId, true));
        setItems(events);
    }

    public void reloadForDay(LocalDate date) {
        List<Event> eventsOnDay = new ArrayList<>();
        Collection<Event> events = eventService.getEventsForWeek(date, userId);
        for (Event event : events) {
            if (TimeService.InstantToLocalDate(event.getTimestamp()).equals(date))
                eventsOnDay.add(event);
        }
        setItems(eventsOnDay);
    }
}
