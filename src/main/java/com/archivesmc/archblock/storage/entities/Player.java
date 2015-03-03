package com.archivesmc.archblock.storage.entities;

import java.util.UUID;

public class Player {
    private String uuid;
    private String username;

    public Player() {}

    public Player(String uuid, String username) {
        this.uuid = uuid;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    // Custom stuff

    public UUID getCastedUuid() {
        return UUID.fromString(this.getUuid());
    }

    public void setCastedUuid(UUID uuid) {
        this.uuid = uuid.toString();
    }
}
