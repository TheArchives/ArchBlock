package com.archivesmc.archblock.storage.entities;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table
@SuppressFBWarnings(value="UWF_UNWRITTEN_FIELD", justification="Framework")
public class Block {
    private Long id;
    private Long x;
    private Long y;
    private Long z;

    private String uuid;
    private String world;

    public Block() {}

    @Id
    @Column(name="id")
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name="x")
    public Long getX() {
        return x;
    }

    public void setX(Long x) {
        this.x = x;
    }

    @Column(name="y")
    public Long getY() {
        return y;
    }

    public void setY(Long y) {
        this.y = y;
    }

    @Column(name="z")
    public Long getZ() {
        return z;
    }

    public void setZ(Long z) {
        this.z = z;
    }

    @Column(name="world")
    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    @Column(name="uuid")
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
