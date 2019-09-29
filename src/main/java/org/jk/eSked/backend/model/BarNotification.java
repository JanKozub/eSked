package org.jk.eSked.backend.model;

import java.time.LocalDateTime;

public class BarNotification {
    private final String eventTopic;
    private final LocalDateTime time;

    public BarNotification(String eventTopic, LocalDateTime time) {
        this.eventTopic = eventTopic;
        this.time = time;
    }

    public String getEventTopic() {
        return eventTopic;
    }

    public LocalDateTime getTime() {
        return time;
    }
}
