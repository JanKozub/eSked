package org.jk.eSked.backend.service.user;

import org.jk.eSked.backend.dao.EventsDao;
import org.jk.eSked.backend.model.Event;
import org.jk.eSked.backend.repositories.EventDB;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.UUID;

@Service
public class EventService implements EventDB {

    private final EventsDao eventsDao;

    public EventService(EventsDao eventsDao) {
        this.eventsDao = eventsDao;
    }

    @Override
    public Collection<Event> getEventsForWeek(LocalDate startOfWeek, UUID userId) {
        return eventsDao.getEventsForWeek(getStartOfWeekMillis(startOfWeek), userId);
    }

    @Override
    public Collection<Event> getEvents(UUID userId) {
        return eventsDao.getEvents(userId);
    }

    @Override
    public void addEvent(Event event) {
        eventsDao.persistEvent(event);
    }

    @Override
    public void deleteEvent(UUID userId, UUID eventId) {
        eventsDao.deleteEvent(userId, eventId);
    }

    @Override
    public void setCheckedFlag(UUID eventId, UUID userId, boolean newState) {
        eventsDao.setCheckedFlag(eventId, userId, newState);
    }

    @Override
    public boolean doesUUIDExists(UUID newUUID) {
        return eventsDao.getUUIDs(newUUID).size() > 0;
    }

    private long getStartOfWeekMillis(LocalDate startOfWeek) {
        return startOfWeek.atTime(0, 0)
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
    }

    public UUID createEventId() {
        UUID randomUUID = UUID.randomUUID();
        while (doesUUIDExists(randomUUID)) randomUUID = UUID.randomUUID();
        return randomUUID;
    }
}
