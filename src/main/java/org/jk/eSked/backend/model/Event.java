package org.jk.eSked.backend.model;

import org.jk.eSked.backend.model.types.EventType;

import java.util.Objects;
import java.util.UUID;

public class Event {
    private UUID creatorId;
    private UUID eventId;
    private EventType type;
    private String topic;
    private int hour;
    private long timestamp;
    private long createdTimestamp;

    public Event(UUID creatorId, UUID eventId, EventType type, String topic, int hour, long timestamp, long createdTimestamp) {
        this.creatorId = creatorId;
        this.eventId = eventId;
        this.type = type;
        this.topic = topic;
        this.hour = hour;
        this.timestamp = timestamp;
        this.createdTimestamp = createdTimestamp;
    }

    public UUID getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(UUID creatorId) {
        this.creatorId = creatorId;
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return hour == event.hour &&
                timestamp == event.timestamp &&
                createdTimestamp == event.createdTimestamp &&
                Objects.equals(creatorId, event.creatorId) &&
                Objects.equals(eventId, event.eventId) &&
                type == event.type &&
                Objects.equals(topic, event.topic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(creatorId, eventId, type, topic, hour, timestamp, createdTimestamp);
    }
}
