package org.jk.eSked.backend.service.user;

import org.jk.eSked.backend.dao.EventsDao;
import org.jk.eSked.backend.model.Event;
import org.jk.eSked.backend.model.schedule.ScheduleEvent;
import org.jk.eSked.backend.repositories.EventDB;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
public class EventService implements EventDB {

    private final EventsDao eventsDao;

    public EventService(EventsDao eventsDao) {
        this.eventsDao = eventsDao;
    }

    @Override
    public Collection<Event> getEvents(LocalDate startOfWeek, UUID userId) {
        return parseScheduleEvents(eventsDao.getEvents(getStartOfWeekMillis(startOfWeek), userId));
    }

    @Override
    public Collection<Event> getAllEvents(UUID userId) {
        return parseScheduleEvents(eventsDao.getAllEvents(userId));
    }

    @Override
    public void addEvent(ScheduleEvent event) {
        eventsDao.persistEvent(event);
    }

    @Override
    public void deleteEvent(ScheduleEvent event) {
        eventsDao.deleteEvent(event);
    }

    private Collection<Event> parseScheduleEvents(Collection<ScheduleEvent> scheduleEvents) {
        List<Event> events = new ArrayList<>();
        for (ScheduleEvent scheduleEvent : scheduleEvents) {
            events.add(new Event(scheduleEvent.getId(),
                    scheduleEvent.getDateTimestamp(),
                    scheduleEvent.getHour(),
                    scheduleEvent.getEventType(),
                    scheduleEvent.getTopic(),
                    scheduleEvent.getCreatedDateTimestamp()));
        }
        return events;
    }

    private long getStartOfWeekMillis(LocalDate startOfWeek) {
        return startOfWeek.atTime(0, 0)
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
    }
}
