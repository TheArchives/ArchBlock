package com.archivesmc.archblock.storage.entities;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table
public class Player {
    private String uuid;
    private String username;

    public Player() {}

    @Column(name="username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Id
    @Column(name="uuid")
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    // Custom stuff

    public UUID uuid() {
        return UUID.fromString(this.getUuid());
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid.toString();
    }
}
