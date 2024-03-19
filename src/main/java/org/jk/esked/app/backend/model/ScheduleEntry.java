package org.jk.esked.app.backend.model;

import jakarta.persistence.*;
import org.jk.esked.app.backend.services.TimeService;

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
    private int hour = 0;

    @Column(name = "\"day\"", nullable = false)
    private int day = 0;

    @Column(name = "subject", nullable = false)
    private String subject = "";

    @Column(name = "created_timestamp", nullable = false)
    private final long created_timestamp = TimeService.now();

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
}
