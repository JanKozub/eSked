package org.jk.esked.app.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.jk.esked.app.backend.model.types.UserType;
import org.jk.esked.app.backend.services.TimeService;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "`USER`")
public class User extends AbstractEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @NotEmpty
    @Column(name = "username", nullable = false)
    private String username = "";

    @NotEmpty
    @Column(name = "password", nullable = false)
    private String password = "";

    @Email
    @NotEmpty
    @Column(name = "email", nullable = false)
    private String email = "";

    @Column(name = "user_type", nullable = false)
    private UserType user_type = UserType.USER;

    @Column(name = "dark_theme", nullable = false)
    private boolean dark_theme = false;

    @Column(name = "schedule_hours", nullable = false)
    private boolean schedule_hours = false;

    @Column(name = "group_code", nullable = false)
    private int group_code = 0;

    @Column(name = "events_sync", nullable = false)
    private boolean events_sync = false;

    @Column(name = "table_sync", nullable = false)
    private boolean table_sync = false;

    @Column(name = "created_timestamp", nullable = false)
    private long created_timestamp = TimeService.now();

    @Column(name = "last_logged_timestamp", nullable = false)
    private long last_logged_timestamp = TimeService.now();

    @Column(name = "verified", nullable = false)
    private boolean verified = false;

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserType getUserType() {
        return user_type;
    }

    public void setUserType(UserType user_type) {
        this.user_type = user_type;
    }

    public boolean isDarkTheme() {
        return dark_theme;
    }

    public void setDarkTheme(boolean dark_theme) {
        this.dark_theme = dark_theme;
    }

    public boolean isScheduleHours() {
        return schedule_hours;
    }

    public void setScheduleHours(boolean schedule_hours) {
        this.schedule_hours = schedule_hours;
    }

    public int getGroupCode() {
        return group_code;
    }

    public void setGroupCode(int group_code) {
        this.group_code = group_code;
    }

    public boolean isEventSync() {
        return events_sync;
    }

    public void setEventsSync(boolean events_sync) {
        this.events_sync = events_sync;
    }

    public boolean isTableSync() {
        return table_sync;
    }

    public void setTableSync(boolean table_sync) {
        this.table_sync = table_sync;
    }

    public long getCreatedTimestamp() {
        return created_timestamp;
    }

    public void setCreatedTimestamp(long created_timestamp) {
        this.created_timestamp = created_timestamp;
    }

    public long getLastLoggedTimestamp() {
        return last_logged_timestamp;
    }

    public void setLastLoggedTimestamp(long last_logged_timestamp) {
        this.last_logged_timestamp = last_logged_timestamp;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return dark_theme == user.dark_theme && schedule_hours == user.schedule_hours && group_code == user.group_code && events_sync == user.events_sync && table_sync == user.table_sync && created_timestamp == user.created_timestamp && last_logged_timestamp == user.last_logged_timestamp && verified == user.verified && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), username, password, dark_theme, schedule_hours, email, group_code, events_sync, table_sync, created_timestamp, last_logged_timestamp, verified);
    }
}
