package org.jk.eSked.backend.repositories;

import org.jk.eSked.backend.model.event.Event;
import org.jk.eSked.backend.model.event.ScheduleEvent;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

public interface EventDB {

    Collection<Event> getEvents(LocalDate startOfWeek, UUID userId);

    Collection<Event> getAllEvents(UUID userId);

    void addEvent(ScheduleEvent event);

    void deleteEvent(ScheduleEvent event);
}
