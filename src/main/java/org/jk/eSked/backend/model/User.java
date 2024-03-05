package org.jk.eSked.backend.model;

import java.security.MessageDigest;
import java.util.Objects;
import java.util.UUID;

public class User {
    private final UUID id;
    private final String username;
    private String password;
    private final boolean darkTheme;
    private final boolean scheduleHours;
    private String email;
    private final int groupCode;
    private final boolean eventsSyn;
    private final boolean tableSyn;
    private final long createdDate;
    private final long LastLoggedDate;
    private final boolean verified;

    public User(UUID id, String username, String password, boolean darkTheme, boolean scheduleHours, String email, int groupCode, boolean eventsSyn, boolean tableSyn, long createdDate, long lastLoggedDate, boolean verified) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.darkTheme = darkTheme;
        this.scheduleHours = scheduleHours;
        this.email = email;
        this.groupCode = groupCode;
        this.eventsSyn = eventsSyn;
        this.tableSyn = tableSyn;
        this.createdDate = createdDate;
        LastLoggedDate = lastLoggedDate;
        this.verified = verified;
    }

    public static String encodePassword(String password) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-1").digest(password.getBytes());

            final StringBuilder builder = new StringBuilder();
            for (byte b : digest) {
                builder.append(String.format("%02x", b));
            }
            return builder.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isDarkTheme() {
        return darkTheme;
    }

    public boolean isScheduleHours() {
        return scheduleHours;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGroupCode() {
        return groupCode;
    }

    public boolean isEventsSyn() {
        return eventsSyn;
    }

    public boolean isTableSyn() {
        return tableSyn;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public boolean isVerified() {
        return verified;
    }

    public long getLastLoggedDate() {
        return LastLoggedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return darkTheme == user.darkTheme &&
                scheduleHours == user.scheduleHours &&
                groupCode == user.groupCode &&
                eventsSyn == user.eventsSyn &&
                tableSyn == user.tableSyn &&
                createdDate == user.createdDate &&
                LastLoggedDate == user.LastLoggedDate &&
                verified == user.verified &&
                Objects.equals(id, user.id) &&
                Objects.equals(username, user.username) &&
                Objects.equals(password, user.password) &&
                Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, darkTheme, scheduleHours, email, groupCode, eventsSyn, tableSyn, createdDate, LastLoggedDate, verified);
    }
}