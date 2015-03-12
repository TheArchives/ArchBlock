package com.archivesmc.archblock.storage.entities;

import java.util.UUID;

/**
 * Entity class that represents a player in the database.
 */
public class Entity {
    private String entityUuid;
    private String ownerUuid;

    public Entity() {}

    public Entity(String entityUuid, String ownerUuid) {
        this.entityUuid = entityUuid;
        this.ownerUuid = ownerUuid;
    }

    public String getOwnerUuid() {
        return ownerUuid;
    }

    public void setOwnerUuid(String username) {
        this.ownerUuid = username;
    }


    public String getEntityUuid() {
        return entityUuid;
    }

    public void setEntityUuid(String entityUuid) {
        this.entityUuid = entityUuid;
    }

    // Custom stuff

    public UUID getCastedUuid() {
        return UUID.fromString(this.getEntityUuid());
    }

    public void setCastedUuid(UUID uuid) {
        this.entityUuid = uuid.toString();
    }
}
