package org.jk.esked.app.frontend.components.events;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.renderer.BasicRenderer;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.jk.esked.app.backend.model.entities.Event;
import org.jk.esked.app.backend.model.entities.ScheduleEntry;
import org.jk.esked.app.backend.services.EventService;
import org.jk.esked.app.backend.services.ScheduleEntryService;
import org.jk.esked.app.backend.services.utilities.TimeService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

public class EventGrid extends Grid<Event> {
    private final ScheduleEntryService scheduleEntryService;
    private final EventService eventService;
    private final UUID userId;
    private LocalDate startOfWeek;

    public EventGrid(UUID userId, ScheduleEntryService scheduleEntryService, EventService eventService, LocalDate startOfWeek) {
        this.scheduleEntryService = scheduleEntryService;
        this.eventService = eventService;
        this.userId = userId;
        this.startOfWeek = startOfWeek;

        addColumn(event -> getTranslation(event.getEventType().getDescription())).setHeader(getTranslation("type"));
        addColumn(new BasicRenderer<>(this::getLesson) {
        }).setHeader(getTranslation("events.hour.header"));
        addColumn(new BasicRenderer<>(this::getDay) {
        }).setHeader(getTranslation("day"));
        addColumn(event -> TimeService.timestampToFormatedString(event.getTimestamp())).setHeader(getTranslation("date"));
        addColumn(Event::getTopic).setHeader(getTranslation("topic"));
        addColumn(new ComponentRenderer<>(this::getIcon)).setHeader(getTranslation("delete"));

        getColumns().forEach(column -> column.setAutoWidth(true));
        setSelectionMode(SelectionMode.NONE);
        setAllRowsVisible(true);
    }

    private String getLesson(Event event) {
        ScheduleEntry entry = scheduleEntryService.findByUserIdAndDayAndHour(userId, TimeService.timestampToDayOfWeek(event.getTimestamp()) - 1, event.getHour());
        return entry != null ? entry.getSubject() + "(" + (entry.getHour() + 1) + ")" : getTranslation("no.entry");
    }

    private String getDay(Event event) {
        return getTranslation("day." + TimeService.timestampToDayOfWeek(event.getTimestamp()));
    }

    private Icon getIcon(Event event) {
        Icon icon = new Icon(VaadinIcon.TRASH);
        icon.getStyle().set("cursor", "pointer");
        icon.addClickListener(e -> {
            eventService.deleteEvent(event.getId());
            reloadForWeek();
        });
        return icon;
    }

    public void setStartOfWeek(LocalDate startOfWeek) {
        this.startOfWeek = startOfWeek;
    }

    public void reloadForWeek() {
        Collection<Event> events = eventService.findByStarOfWeek(userId, startOfWeek);
        events.stream().filter(event -> !event.isCheckedFlag())
                .forEach(event -> eventService.changeCheckedFlag(event.getId(), true));
        setItems(events);
    }

    public void reloadForDay(LocalDate date) {
        setItems(eventService.findEventsOnDay(userId, date));
    }
}
