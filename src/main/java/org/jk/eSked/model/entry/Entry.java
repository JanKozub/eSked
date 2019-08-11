package org.jk.eSked.model.entry;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Objects;

public class Entry {
    private final int hour;
    private final int day;
    private final String subject;
    private final LocalDate createdDate;

    public Entry(int hour, int day, String subject, long createdDate) {
        this.hour = hour;
        this.day = day;
        this.subject = subject;
        this.createdDate = Instant.ofEpochMilli(createdDate).atZone(ZoneOffset.systemDefault()).toLocalDate();
    }

    public int getHour() {
        return hour;
    }

    public int getDay() {
        return day;
    }

    public String getSubject() {
        return subject;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entry entry = (Entry) o;
        return hour == entry.hour &&
                day == entry.day &&
                Objects.equals(subject, entry.subject) &&
                Objects.equals(createdDate, entry.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hour, day, subject, createdDate);
    }
}
