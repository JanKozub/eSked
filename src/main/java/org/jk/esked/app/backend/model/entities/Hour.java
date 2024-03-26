package org.jk.esked.app.backend.model.entities;

import com.helger.commons.annotation.Nonempty;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "\"hour\"")
public class Hour extends AbstractEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "\"hour\"", nullable = false)
    private int hour = 0;

    @Nonempty
    @Column(name = "data", nullable = false)
    private String data = "";

    public Hour(User user, int hour, String data) {
        this.user = user;
        this.hour = hour;
        this.data = data;
    }

    public Hour() {}

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

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
