package org.jk.eSked.backend.repositories;

import org.jk.eSked.backend.model.Event;
import org.jk.eSked.backend.model.schedule.ScheduleEvent;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

public interface EventDB {

    Collection<Event> getEvents(LocalDate startOfWeek, UUID userId);

    Collection<Event> getAllEvents(UUID userId);

    void addEvent(ScheduleEvent event);

    void deleteEvent(ScheduleEvent event);
}
