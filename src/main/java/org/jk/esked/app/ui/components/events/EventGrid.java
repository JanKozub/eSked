package org.jk.esked.app.ui.components.events;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.renderer.BasicRenderer;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.jk.esked.app.backend.model.Event;
import org.jk.esked.app.backend.model.ScheduleEntry;
import org.jk.esked.app.backend.services.EventService;
import org.jk.esked.app.backend.services.ScheduleService;
import org.jk.esked.app.backend.services.TimeService;

import java.time.LocalDate;
import java.util.*;

public class EventGrid extends Grid<Event> {
    private final EventService eventService;
    private final UUID userId;
    private LocalDate startOfWeek;

    public EventGrid(UUID userId, ScheduleService scheduleService, EventService eventService, LocalDate startOfWeek) {
        this.eventService = eventService;
        this.userId = userId;
        this.startOfWeek = startOfWeek;

        Collection<ScheduleEntry> entries = scheduleService.getScheduleEntriesByUserId(userId);

        setSelectionMode(SelectionMode.NONE);
        setWidth("100%");
        getColumns().forEach(column -> column.setAutoWidth(true));
        setAllRowsVisible(true);

        addColumn(event -> getTranslation(event.getEventType().getDescription())).setHeader(getTranslation("type"));
        addColumn(new BasicRenderer<>(event -> {
            for (ScheduleEntry entry : entries) {
                if (entry.getHour() == event.getHour() && entry.getDay() == TimeService.instantToLocalDate(event.getTimestamp()).getDayOfWeek().getValue() - 1)
                    return entry.getSubject() + "(" + entry.getHour() + ")";
            }
            return getTranslation("no.entry");
        }) {}).setHeader(getTranslation("events.hour.header"));
        addColumn(new ComponentRenderer<>(event -> {
            Calendar calendar = Calendar.getInstance(new Locale("en", "UK"));
            calendar.setTimeInMillis(event.getTimestamp());
            return new Text(getTranslation("day." + (calendar.get(Calendar.DAY_OF_WEEK) - 1)));
        }));
        addColumn(event -> TimeService.instantToFormattedDate(event.getTimestamp())).setHeader(getTranslation("date"));
        addColumn(Event::getTopic).setHeader(getTranslation("topic"));
        addColumn(new ComponentRenderer<>(e -> {
            Icon icon = new Icon(VaadinIcon.TRASH);
            icon.getStyle().set("cursor", "pointer");
            icon.addClickListener(event -> {
                eventService.deleteEvent(e.getId());
                reloadForWeek();
            });
            return icon;
        })).setHeader(getTranslation("delete"));
    }

    public void setStartOfWeek(LocalDate startOfWeek) {
        this.startOfWeek = startOfWeek;
    }

    public void reloadForWeek() {
        Collection<Event> events = eventService.getEventsForWeek(userId, startOfWeek);
        events.stream().filter(event -> !event.isCheckedFlag())
                .forEach(event -> eventService.setCheckedFlag(event.getId(), true));
        setItems(events);
    }

    public void reloadForDay(LocalDate date) {
        List<Event> eventsOnDay = new ArrayList<>();
        for (Event event : eventService.getEventsForWeek(userId, date)) {
            if (TimeService.instantToLocalDate(event.getTimestamp()).equals(date))
                eventsOnDay.add(event);
        }
        setItems(eventsOnDay);
    }
}
