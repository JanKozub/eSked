package org.jk.eSked.model.entry;

import java.util.Objects;
import java.util.UUID;

public class ScheduleEntry {
    private final UUID userId;
    private final int hour;
    private final int day;
    private final String subject;
    private final long createdTimestamp;

    public ScheduleEntry(UUID userId, int hour, int day, String subject, long createdDate) {
        this.userId = userId;
        this.hour = hour;
        this.subject = subject;
        this.day = day;
        this.createdTimestamp = createdDate;
    }

    public int getHour() {
        return hour;
    }

    public String getSubject() {
        return subject;
    }

    public int getDay() {
        return day;
    }

    public long getCreatedTimestamp() {
        return createdTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleEntry entry = (ScheduleEntry) o;
        return hour == entry.hour &&
                day == entry.day &&
                userId.equals(entry.userId) &&
                subject.equals(entry.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, hour, day, subject);
    }

    @Override
    public String toString() {
        return subject;
    }

}
