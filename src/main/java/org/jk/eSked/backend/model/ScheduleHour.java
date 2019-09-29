package org.jk.eSked.backend.model;

import java.util.UUID;

public class ScheduleHour {
    private final UUID userId;
    private final int hour;
    private final String data;

    public ScheduleHour(UUID userId, int hour, String data) {
        this.userId = userId;
        this.hour = hour;
        this.data = data;
    }

    public UUID getUserId() {
        return userId;
    }

    public int getHour() {
        return hour;
    }

    public String getData() {
        return data;
    }
}
