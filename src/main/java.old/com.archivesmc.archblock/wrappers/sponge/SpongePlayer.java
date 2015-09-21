package com.archivesmc.archblock.wrappers.sponge;

import com.archivesmc.archblock.wrappers.Player;
import org.spongepowered.api.text.chat.ChatTypes;

import java.util.UUID;

public class SpongePlayer implements Player {
    org.spongepowered.api.entity.player.Player player;

    public SpongePlayer(org.spongepowered.api.entity.player.Player player) {
        this.player = player;
    }

    @Override
    public UUID getUniqueId() {
        return this.player.getUniqueId();
    }

    @Override
    public String getName() {
        return this.player.getName();
    }

    @Override
    public void sendMessage(String message) {
        this.player.sendMessage(ChatTypes.SYSTEM, message);
    }

    @Override
    public Object getWrapped() {
        return this.player;
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.player.hasPermission(permission);
    }
}
