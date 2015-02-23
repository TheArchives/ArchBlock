package com.archivesmc.archblock.storage.entities;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table
@SuppressFBWarnings(value="UWF_UNWRITTEN_FIELD", justification="Framework")
public class Friendship {
    private Long id;
    private String playerUuid;
    private String friendUuid;

    public Friendship() {}

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

    @Column(name="player_uuid")
    public String getPlayerUuid() {
        return this.playerUuid;
    }

    public void setPlayerUuid(String playerUuid) {
        this.playerUuid = playerUuid;
    }

    @Column(name="friend_uuid")
    public String getFriendUuid() {
        return this.friendUuid;
    }

    public void setFriendUuid(String friendUuid) {
        this.friendUuid = friendUuid;
    }
}
