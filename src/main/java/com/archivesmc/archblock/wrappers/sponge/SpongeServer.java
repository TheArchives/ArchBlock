package com.archivesmc.archblock.wrappers.sponge;

import com.archivesmc.archblock.wrappers.Player;
import com.archivesmc.archblock.wrappers.Server;

import java.util.UUID;

public class SpongeServer implements Server {
    @Override
    public Player getPlayer(String player) {
        return null;
    }

    @Override
    public Player getPlayer(UUID id) {
        return null;
    }

    @Override
    public Object getWrapped() {
        return null;
    }
}
