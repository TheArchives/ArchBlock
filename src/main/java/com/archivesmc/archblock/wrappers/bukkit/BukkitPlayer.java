package com.archivesmc.archblock.wrappers.bukkit;

import com.archivesmc.archblock.wrappers.Player;

import java.util.UUID;

public class BukkitPlayer implements Player {
    private org.bukkit.entity.Player wrappedPlayer;

    public BukkitPlayer(org.bukkit.entity.Player player) {
        this.wrappedPlayer = player;
    }

    @Override
    public UUID getUniqueId() {
        return this.wrappedPlayer.getUniqueId();
    }

    @Override
    public String getName() {
        return this.wrappedPlayer.getName();
    }

    @Override
    public void sendMessage(String message) {
        this.wrappedPlayer.sendMessage(message);
    }

    @Override
    public Object getWrapped() {
        return this.wrappedPlayer;
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.wrappedPlayer.hasPermission(permission);
    }
}
