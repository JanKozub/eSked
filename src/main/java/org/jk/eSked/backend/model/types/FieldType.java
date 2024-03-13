package org.jk.eSked.backend.model.types;

public enum FieldType {
    USERNAME("username"),
    EMAIL("email"),
    PASSWORD("password"),
    GROUP("group");


    private final String description;

    FieldType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
