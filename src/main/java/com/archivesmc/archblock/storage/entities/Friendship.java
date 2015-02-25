package com.archivesmc.archblock.storage.entities;

public class Friendship {
    private Long id;
    private String playerUuid;
    private String friendUuid;

    public Friendship() {}


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getPlayerUuid() {
        return this.playerUuid;
    }

    public void setPlayerUuid(String playerUuid) {
        this.playerUuid = playerUuid;
    }


    public String getFriendUuid() {
        return this.friendUuid;
    }

    public void setFriendUuid(String friendUuid) {
        this.friendUuid = friendUuid;
    }
}
