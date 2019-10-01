package org.jk.eSked.backend.model.types;

public enum NotificationType {
    SHORT(5000),
    LONG(15000);

    private final int duration;

    NotificationType(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }
}
