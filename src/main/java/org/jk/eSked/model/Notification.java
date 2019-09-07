package org.jk.eSked.model;

import java.time.LocalDateTime;

public class Notification {
    private final String eventTopic;
    private final LocalDateTime time;

    public Notification(String eventTopic, LocalDateTime time) {
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
