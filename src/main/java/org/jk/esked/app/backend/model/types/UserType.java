package org.jk.esked.app.backend.model.types;

public enum UserType {
    USER("USER"),
    ADMIN("ADMIN");

    private final String description;

    UserType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
