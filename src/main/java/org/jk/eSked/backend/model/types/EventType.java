package org.jk.eSked.backend.model.types;

public enum EventType {
    HOMEWORK("Zadanie"),
    QUESTIONS("Pytanie"),
    QUIZ("Kartkówka"),
    TEST("Sprawdzian");

    private final String description;

    EventType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
