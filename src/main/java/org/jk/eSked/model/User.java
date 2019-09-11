package org.jk.eSked.model;

import java.security.MessageDigest;
import java.util.Objects;
import java.util.UUID;

public class User {
    private final UUID id;
    private final String username;
    private final String password;
    private final boolean darkTheme;
    private final boolean scheduleHours;
    private final String email;
    private final int groupCode;
    private final boolean eventsSyn;
    private final boolean tableSyn;
    private final long createdDate;
    private final long LastLoggedDate;
    private final int genCode;

    public User(UUID id, String username, String password, boolean darkTheme, boolean scheduleHours, String email, int groupCode, boolean eventsSyn, boolean tableSyn, long createdDate, long lastLoggedDate, int genCode) {
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
        this.genCode = genCode;
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

    public boolean isDarkTheme() {
        return darkTheme;
    }

    public boolean isScheduleHours() {
        return scheduleHours;
    }

    public String getEmail() {
        return email;
    }

    public int getGroupCode() {
        return groupCode;
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

    public boolean isEventsSyn() {
        return eventsSyn;
    }

    public boolean isTableSyn() {
        return tableSyn;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public int getGenCode() {
        return genCode;
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
                genCode == user.genCode &&
                Objects.equals(id, user.id) &&
                Objects.equals(username, user.username) &&
                Objects.equals(password, user.password) &&
                Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, darkTheme, scheduleHours, email, groupCode, eventsSyn, tableSyn, createdDate, LastLoggedDate, genCode);
    }
}