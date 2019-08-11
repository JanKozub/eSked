package org.jk.eSked.services.events;

import org.jk.eSked.model.event.Event;
import org.jk.eSked.model.event.ScheduleEvent;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

public interface EventService {

    Collection<Event> getEvents(LocalDate startOfWeek, UUID userId);

    Collection<Event> getAllEvents(UUID userId);

    void addEvent(ScheduleEvent event);

    void deleteEvent(ScheduleEvent event);
}
