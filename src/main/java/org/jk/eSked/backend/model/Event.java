package org.jk.eSked.backend.model;

import org.jk.eSked.backend.model.types.EventType;

import java.util.Objects;
import java.util.UUID;

public class Event {
    private UUID userId;
    private final UUID eventId;
    private EventType type;
    private String topic;
    private int hour;
    private boolean checkedFlag;
    private long timestamp;
    private final long createdTimestamp;

    public Event(UUID userId, UUID eventId, EventType type, String topic, int hour, boolean checkedFlag, long timestamp, long createdTimestamp) {
        this.userId = userId;
        this.eventId = eventId;
        this.type = type;
        this.topic = topic;
        this.hour = hour;
        this.checkedFlag = checkedFlag;
        this.timestamp = timestamp;
        this.createdTimestamp = createdTimestamp;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getEventId() {
        return eventId;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public boolean isCheckedFlag() {
        return checkedFlag;
    }

    public void setCheckedFlag(boolean checkedFlag) {
        this.checkedFlag = checkedFlag;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return hour == event.hour &&
                checkedFlag == event.checkedFlag &&
                timestamp == event.timestamp &&
                createdTimestamp == event.createdTimestamp &&
                Objects.equals(userId, event.userId) &&
                Objects.equals(eventId, event.eventId) &&
                type == event.type &&
                Objects.equals(topic, event.topic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, eventId, type, topic, hour, checkedFlag, timestamp, createdTimestamp);
    }
}
