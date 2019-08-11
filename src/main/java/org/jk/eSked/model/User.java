package org.jk.eSked.model;

import java.security.MessageDigest;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
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
    private final boolean synWGroup;
    private final LocalDate createdDate;
    private final LocalDate LastLoggedDate;

    public User(UUID id, String username, String password, boolean darkTheme, boolean scheduleHours, String email, int groupCode, boolean synWGroup, long timestampAccCreated, long timestampLastLogged) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.darkTheme = darkTheme;
        this.scheduleHours = scheduleHours;
        this.email = email;
        this.groupCode = groupCode;
        this.synWGroup = synWGroup;
        this.createdDate = Instant.ofEpochMilli(timestampAccCreated).atZone(ZoneOffset.systemDefault()).toLocalDate();
        this.LastLoggedDate = Instant.ofEpochMilli(timestampLastLogged).atZone(ZoneOffset.systemDefault()).toLocalDate();
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

    public boolean isSynWGroup() {
        return synWGroup;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public LocalDate getLastLoggedDate() {
        return LastLoggedDate;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return darkTheme == user.darkTheme &&
                scheduleHours == user.scheduleHours &&
                groupCode == user.groupCode &&
                Objects.equals(id, user.id) &&
                Objects.equals(username, user.username) &&
                Objects.equals(password, user.password) &&
                Objects.equals(email, user.email) &&
                Objects.equals(createdDate, user.createdDate) &&
                Objects.equals(LastLoggedDate, user.LastLoggedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, darkTheme, scheduleHours, email, groupCode, createdDate, LastLoggedDate);
    }
}