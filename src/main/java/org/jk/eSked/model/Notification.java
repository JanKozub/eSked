package org.jk.eSked.model;

import java.time.LocalDateTime;

public class Notification {
    private String eventTopic;
    private LocalDateTime time;

    public Notification(String eventTopic, LocalDateTime time) {
        this.eventTopic = eventTopic;
        this.time = time;
    }

    public String getEventTopic() {
        return eventTopic;
    }

    public void setEventTopic(String eventTopic) {
        this.eventTopic = eventTopic;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
