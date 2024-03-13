package org.jk.eSked.backend.model.types;

public enum EventType {
    HOMEWORK("homework"),
    QUESTIONS("questions"),
    QUIZ("quiz"),
    TEST("test");

    private final String description;

    EventType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
