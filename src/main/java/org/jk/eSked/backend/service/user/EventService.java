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
    public Collection<Event> getEventsForWeek(LocalDate startOfWeek, UUID creatorId) {
        return eventsDao.getEventsForWeek(getStartOfWeekMillis(startOfWeek), creatorId);
    }

    @Override
    public Collection<Event> getEvents(UUID creatorId) {
        return eventsDao.getEvents(creatorId);
    }

    @Override
    public void addEvent(Event event) {
        eventsDao.persistEvent(event);
    }

    @Override
    public void deleteEvent(UUID creatorId, UUID eventId) {
        eventsDao.deleteEvent(creatorId, eventId);
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
