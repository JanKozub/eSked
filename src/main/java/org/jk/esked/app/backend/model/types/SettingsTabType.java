package org.jk.esked.app.backend.model.types;

public enum SettingsTabType {
    ACCOUNT("account"),
    GROUP("group"),
    OTHER("other");

    private final String description;

    SettingsTabType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
