package com.archivesmc.archblock.wrappers;

import java.util.UUID;

public interface Server {
    Player getPlayer(String name);
    Player getPlayer(UUID id);

    Object getWrapped();
}
