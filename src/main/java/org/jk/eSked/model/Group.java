package org.jk.eSked.model;

import java.time.LocalDate;
import java.util.UUID;

public class Group {
    private final boolean isAccepted;
    private final String name;
    private final int code;
    private final UUID leaderId;
    private final LocalDate createdDate;

    public Group(boolean isAccepted, String name, int code, UUID leaderId, LocalDate createdDate) {
        this.isAccepted = isAccepted;
        this.name = name;
        this.code = code;
        this.leaderId = leaderId;
        this.createdDate = createdDate;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    public UUID getLeaderId() {
        return leaderId;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }
}
