package org.jk.esked.app.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.jk.esked.app.backend.services.TimeService;

import java.util.UUID;

@Entity
@Table(name = "message")
public class Message extends AbstractEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "timestamp", nullable = false)
    private long timestamp = TimeService.now();

    @Column(name = "text", nullable = false)
    private String text = "";

    @Column(name = "checked_flag", nullable = false)
    private boolean checked_flag = false;

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
        return checked_flag;
    }

    public void setCheckedFlag(boolean checked_flag) {
        this.checked_flag = checked_flag;
    }
}
