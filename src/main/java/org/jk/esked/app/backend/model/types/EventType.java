package org.jk.esked.app.backend.model.types;

public enum EventType {
    HOMEWORK("homework", "#46c768"),
    QUESTIONS("questions", "#ebbf23"),
    QUIZ("quiz", "#e88133"),
    TEST("test", "#c43737");

    private final String description;
    private final String color;

    EventType(String description, String color) {
        this.description = description;
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public String getColor() {
        return color;
    }
}
