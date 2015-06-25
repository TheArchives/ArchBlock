package com.archivesmc.archblock.wrappers.sponge;

import com.archivesmc.archblock.wrappers.Player;
import com.archivesmc.archblock.wrappers.Server;
import com.google.common.base.Optional;

import java.util.UUID;

public class SpongeServer implements Server {
    org.spongepowered.api.Server server;

    public SpongeServer(org.spongepowered.api.Server server) {
        this.server = server;
    }

    @Override
    public Player getPlayer(String player) {
        Optional<org.spongepowered.api.entity.player.Player> maybePlayer = this.server.getPlayer(player);

        if (maybePlayer.isPresent()) {
            return new SpongePlayer(maybePlayer.get());
        } else {
            return null;
        }
    }

    @Override
    public Player getPlayer(UUID id) {
        Optional<org.spongepowered.api.entity.player.Player> maybePlayer = this.server.getPlayer(id);

        if (maybePlayer.isPresent()) {
            return new SpongePlayer(maybePlayer.get());
        } else {
            return null;
        }
    }

    @Override
    public Object getWrapped() {
        return this.server;
    }
}
