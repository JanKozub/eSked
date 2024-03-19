package org.jk.esked.app.backend.model;

import jakarta.persistence.*;
import org.jk.esked.app.backend.services.TimeService;

import java.util.UUID;

@Entity
@Table(name = "\"group\"")
public class Group extends AbstractEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "leader_id", nullable = false)
    private User leader;

    @Column(name = "name", nullable = false)
    private String name = "";

    @Column(name = "group_code", nullable = false)
    private int group_code = 0;

    @Column(name = "is_accepted", nullable = false)
    private boolean is_accepted = false;

    @Column(name = "created_date", nullable = false)
    private long created_date = TimeService.now();

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    public User getLeader() {
        return leader;
    }

    public void setLeader(User leader) {
        this.leader = leader;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGroupCode() {
        return group_code;
    }

    public void setGroupCode(int group_code) {
        this.group_code = group_code;
    }

    public boolean isAccepted() {
        return is_accepted;
    }

    public void setIsAccepted(boolean is_accepted) {
        this.is_accepted = is_accepted;
    }
}