package org.jk.eSked.backend.model;

import java.util.UUID;

public class Message {
    private UUID userId;
    private final UUID messageId;
    private long timestamp;
    private String text;
    private boolean checkedFlag;

    public Message(UUID userId, UUID messageId, long timestamp, String text, boolean checkedFlag) {
        this.userId = userId;
        this.messageId = messageId;
        this.timestamp = timestamp;
        this.text = text;
        this.checkedFlag = checkedFlag;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getMessageId() {
        return messageId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCheckedFlag() {
        return checkedFlag;
    }

    public void setCheckedFlag(boolean checkedFlag) {
        this.checkedFlag = checkedFlag;
    }
}
