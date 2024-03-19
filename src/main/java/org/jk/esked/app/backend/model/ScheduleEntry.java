package org.jk.esked.app.backend.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "schedule_entries")
public class ScheduleEntry extends AbstractEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "\"hour\"", nullable = false)
    private int hour;

    @Column(name = "\"day\"", nullable = false)
    private int day;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "created_timestamp", nullable = false)
    private long created_timestamp = 0;

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public long getCreatedTimestamp() {
        return created_timestamp;
    }

    public void setCreatedTimestamp(long created_timestamp) {
        this.created_timestamp = created_timestamp;
    }
}
