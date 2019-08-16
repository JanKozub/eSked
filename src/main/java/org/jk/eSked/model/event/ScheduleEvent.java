package org.jk.eSked.model.event;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.UUID;

public class ScheduleEvent {
    private final UUID userId;
    private final UUID id;
    private final LocalDate date;
    private final long dateTimestamp;
    private final int hour;
    private final EventType eventType;
    private final String topic;
    private final LocalDate createdDate;
    private final long createdDateTimestamp;

    public ScheduleEvent(UUID userId, UUID id, long date, int hour, EventType eventType, String topic, long createdDate) {
        this.userId = userId;
        this.id = id;
        this.dateTimestamp = date;
        this.date = Instant.ofEpochMilli(date).atZone(ZoneOffset.systemDefault()).toLocalDate();
        this.hour = hour;
        this.eventType = eventType;
        this.topic = topic;
        this.createdDateTimestamp = createdDate;
        this.createdDate = Instant.ofEpochMilli(createdDate).atZone(ZoneOffset.systemDefault()).toLocalDate();
    }

    public UUID getId() {
        return id;
    }

    public long getDateTimestamp() {
        return dateTimestamp;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getHour() {
        return hour;
    }

    public EventType getEventType() {
        return eventType;
    }

    public String getTopic() {
        return topic;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public UUID getUserId() {
        return userId;
    }

    public long getCreatedDateTimestamp() {
        return createdDateTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleEvent that = (ScheduleEvent) o;
        return hour == that.hour &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(id, that.id) &&
                Objects.equals(date, that.date) &&
                eventType == that.eventType &&
                Objects.equals(topic, that.topic) &&
                Objects.equals(createdDate, that.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, id, date, hour, eventType, topic, createdDate);
    }
}
