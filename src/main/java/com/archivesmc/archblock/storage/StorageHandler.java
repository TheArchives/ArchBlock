package com.archivesmc.archblock.storage;

import com.sk89q.worldedit.entity.Player;

import java.util.UUID;

public interface StorageHandler {
    // TODO: Decide what to put in here

    public Boolean setup();
    public void close();
    
    public void upsertPlayer(Player player);
    public void upsertPlayer(UUID uuid, String username);
}
