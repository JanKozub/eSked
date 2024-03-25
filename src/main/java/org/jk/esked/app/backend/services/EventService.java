package org.jk.esked.app.backend.services;

import org.jk.esked.app.backend.model.entities.Event;
import org.jk.esked.app.backend.model.entities.User;
import org.jk.esked.app.backend.model.types.EventType;
import org.jk.esked.app.backend.repositories.EventRepository;
import org.jk.esked.app.backend.services.utilities.TimeService;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void saveEvent(Event event) {
        eventRepository.save(event);
    }

    public void saveEvent(User user, EventType eventType, String topic, int hour, boolean checkedFlag, long timestamp) {
        eventRepository.save(new Event(user, eventType, topic, hour, checkedFlag, timestamp));
    }

    public List<Event> findByStarOfWeek(UUID userId, LocalDate startOfWeek) {
        return eventRepository.findByStartOfWeek(userId, getStartOfWeekInEpochSeconds(startOfWeek));
    }

    public List<Event> findByUserId(UUID userId) {
        return eventRepository.findByUserId(userId);
    }

    public List<Event> findByUserIdAndHourAndDay(UUID userId, int hour, int day, LocalDate startOfWeek) { //TODO make a query
        List<Event> events = findByStarOfWeek(userId, startOfWeek);

        List<Event> entryEvents = new ArrayList<>();
        for (Event event : events) {
            if (event.getHour() == hour && TimeService.timestampToLocalDateTime(event.getTimestamp()).getDayOfWeek() == DayOfWeek.of(day + 1)) {
                entryEvents.add(event);
            }
        }

        return entryEvents;
    }

    public void deleteEvent(UUID id) {
        eventRepository.deleteById(id);
    }

    public void changeCheckedFlag(UUID eventId, boolean newState) {
        eventRepository.changeCheckedFlag(eventId, newState);
    }

    private long getStartOfWeekInEpochSeconds(LocalDate startOfWeek) {
        return startOfWeek.atTime(0, 0)
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .getEpochSecond();
    }
}
