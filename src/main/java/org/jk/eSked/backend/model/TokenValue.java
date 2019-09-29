package org.jk.eSked.backend.model;

import java.util.UUID;

public class TokenValue {
    private UUID userId;
    private String value;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "TokenValue{" +
                "userId=" + userId +
                ", value=" + value +
                '}';
    }
}
