package org.jk.esked.app.backend.model.types;

public enum EventType {
    HOMEWORK(0, "homework", "#46c768"),
    QUESTIONS(1, "questions", "#ebbf23"),
    QUIZ(2, "quiz", "#e88133"),
    TEST(3, "test", "#c43737");

    private final int id;
    private final String description;
    private final String color;

    EventType(int id, String description, String color) {
        this.id = id;
        this.description = description;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getColor() {
        return color;
    }
}
