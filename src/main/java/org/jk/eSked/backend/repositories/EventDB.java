package org.jk.eSked.backend.repositories;

import org.jk.eSked.backend.model.Event;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

public interface EventDB {

    Collection<Event> getEventsForWeek(LocalDate startOfWeek, UUID userId);

    Collection<Event> getEvents(UUID userId);

    void addEvent(Event event);

    void deleteEvent(UUID userId, UUID eventId);

    void setCheckedFlag(UUID eventId, UUID userId, boolean newState);

    boolean doesUUIDExists(UUID newUUID);
}
