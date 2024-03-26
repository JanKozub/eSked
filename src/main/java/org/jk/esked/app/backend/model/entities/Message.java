package org.jk.esked.app.backend.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.jk.esked.app.backend.services.utilities.TimeService;

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

    public Message(User user, String text) {
        this.user = user;
        this.text = text;
    }

    public Message() {}

    @Override
    public UUID getId() {
        return id;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCheckedFlag() {
        return checked_flag;
    }
}
