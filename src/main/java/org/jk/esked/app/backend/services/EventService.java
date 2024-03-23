package org.jk.esked.app.backend.services;

import org.jk.esked.app.backend.model.entities.Event;
import org.jk.esked.app.backend.model.entities.User;
import org.jk.esked.app.backend.model.types.EventType;
import org.jk.esked.app.backend.repositories.EventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getEventsForWeek(UUID userId, LocalDate startOfWeek) {
        return eventRepository.findEventByUserIdAndWeek(userId, getStartOfWeekInEpochSeconds(startOfWeek));
    }

    public List<Event> getEventsByUserId(UUID userId) {
        return eventRepository.findEventByUserId(userId);
    }

    public void saveEvent(Event event) {
        eventRepository.save(event);
    }

    public void saveEvent(User user, EventType eventType, String topic, int hour, boolean checkedFlag, long timestamp) {
        eventRepository.save(new Event(user, eventType, topic, hour, checkedFlag, timestamp));
    }

    public void deleteEvent(UUID id) {
        eventRepository.deleteById(id);
    }

    public void setCheckedFlag(UUID eventId, boolean newState) {
        eventRepository.updateEventSetCheckedFlagForEvent(eventId, newState);
    }

    private long getStartOfWeekInEpochSeconds(LocalDate startOfWeek) {
        return startOfWeek.atTime(0, 0)
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .getEpochSecond();
    }
}
