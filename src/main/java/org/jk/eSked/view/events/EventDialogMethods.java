package org.jk.eSked.view.events;

import org.jk.eSked.component.SimplePopup;
import org.jk.eSked.model.event.EventType;
import org.jk.eSked.model.event.ScheduleEvent;
import org.jk.eSked.services.events.EventService;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.UUID;

public class EventDialogMethods {
    public EventDialogMethods() {

    }

    public boolean onAdd(EventService eventService, LocalDate eventDate, int hour, EventType eventType, String topic, UUID userID) {
        SimplePopup popup = new SimplePopup();
        if (eventDate != null) {
            if (eventType != null) {
                if (!topic.equals("")) {
                    long time = eventDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
                    UUID id = UUID.randomUUID();
                    ScheduleEvent event = new ScheduleEvent(userID, id, time, hour, eventType, topic, Instant.now().toEpochMilli());
                    eventService.addEvent(event);
                    return true;
                } else popup.open("Pole z nazwą nie może być puste");
            } else popup.open("Pole z typem wydarzenia nie może być puste");
        } else popup.open("Pole z datą nie może być puste");
        return false;
    }
}