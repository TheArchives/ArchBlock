package com.archivesmc.archblock.wrappers.sponge;

import com.archivesmc.archblock.wrappers.Player;

import java.util.UUID;

public class SpongePlayer implements Player {
    @Override
    public UUID getUniqueId() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public Object getWrapped() {
        return null;
    }

    @Override
    public boolean hasPermission(String permission) {
        return false;
    }
}
