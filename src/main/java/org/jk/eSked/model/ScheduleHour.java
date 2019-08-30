package org.jk.eSked.model;

import java.util.UUID;

public class ScheduleHour {
    private UUID userId;
    private int hour;
    private String data;

    public ScheduleHour(UUID userId, int hour, String data) {
        this.userId = userId;
        this.hour = hour;
        this.data = data;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
