package org.jk.eSked.model;

import java.util.UUID;

public class Group {
    private final String name;
    private final int groupCode;
    private final UUID leaderId;
    private final boolean isAccepted;
    private final long createdDate;

    public Group(String name, int groupCode, UUID leaderId, boolean isAccepted, long createdDate) {
        this.name = name;
        this.groupCode = groupCode;
        this.leaderId = leaderId;
        this.isAccepted = isAccepted;
        this.createdDate = createdDate;
    }

    public String getName() {
        return name;
    }

    public int getGroupCode() {
        return groupCode;
    }

    public UUID getLeaderId() {
        return leaderId;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public long getCreatedDate() {
        return createdDate;
    }
}
