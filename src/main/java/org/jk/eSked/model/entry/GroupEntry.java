package org.jk.eSked.model.entry;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.UUID;

public class GroupEntry {
    private final boolean accepted;
    private final String name;
    private final int groupCode;
    private final UUID leaderId;
    private final int hour;
    private final int day;
    private final String subject;
    private final LocalDate createdDate;

    public GroupEntry(boolean accepted, String name, int groupCode, UUID leaderId, int hour, int day, String subject, long timestampCreatedDate) {
        this.accepted = accepted;
        this.name = name;
        this.groupCode = groupCode;
        this.leaderId = leaderId;
        this.hour = hour;
        this.day = day;
        this.subject = subject;
        this.createdDate = Instant.ofEpochMilli(timestampCreatedDate).atZone(ZoneOffset.systemDefault()).toLocalDate();
    }

    public boolean isAccepted() {
        return accepted;
    }

    public String getName() {
        return name;
    }

    public int getGroupCode() {
        return groupCode;
    }

    public UUID getLeaderId() {
        return leaderId;
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
        GroupEntry that = (GroupEntry) o;
        return groupCode == that.groupCode &&
                hour == that.hour &&
                day == that.day &&
                Objects.equals(name, that.name) &&
                Objects.equals(subject, that.subject) &&
                Objects.equals(createdDate, that.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, groupCode, hour, day, subject, createdDate);
    }

    @Override
    public String toString() {
        return subject;
    }
}
