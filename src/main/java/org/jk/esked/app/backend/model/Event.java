package org.jk.esked.app.backend.model;

import jakarta.persistence.*;
import org.jk.esked.app.backend.model.types.EventType;

import java.util.UUID;

@Entity
@Table(name = "event")
public class Event extends AbstractEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "event_type", nullable = false)
    private EventType event_type = EventType.HOMEWORK;

    @Column(name = "topic", nullable = false)
    private String topic = "";

    @Column(name = "\"hour\"", nullable = false)
    private int hour = 0;

    @Column(name = "checked_flag", nullable = false)
    private boolean checked_flag = false;

    @Column(name = "timestamp", nullable = false)
    private long timestamp = 0;

    @Column(name = "created_timestamp", nullable = false)
    private long created_timestamp = 0;

    @Override
    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public EventType getEventType() {
        return event_type;
    }

    public void setEventType(EventType event_type) {
        this.event_type = event_type;
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
        return checked_flag;
    }

    public void setCheckedFlag(boolean checked_flag) {
        this.checked_flag = checked_flag;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getCreatedTimestamp() {
        return created_timestamp;
    }

    public void setCreatedTimestamp(long created_timestamp) {
        this.created_timestamp = created_timestamp;
    }
}
