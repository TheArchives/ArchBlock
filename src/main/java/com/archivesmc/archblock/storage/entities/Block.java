package com.archivesmc.archblock.storage.entities;

import java.util.UUID;
/**
 * Entity class that represents a block in the database.
 */
public class Block {
    private long id;
    private long x;
    private long y;
    private long z;

    private String uuid;
    private String world;

    public Block() {}

    public Block(long x, long y, long z, String uuid, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.uuid = uuid;
        this.world = world;
    }

    public Block(long x, long y, long z, UUID uuid, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.uuid = uuid.toString();
        this.world = world;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }


    public long getY() {
        return y;
    }

    public void setY(long y) {
        this.y = y;
    }


    public long getZ() {
        return z;
    }

    public void setZ(long z) {
        this.z = z;
    }


    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
