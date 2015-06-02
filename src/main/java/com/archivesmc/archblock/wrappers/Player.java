package com.archivesmc.archblock.wrappers;

import java.util.UUID;

public interface Player {
    UUID getUniqueId();
    String getName();
    void sendMessage(String message);

    Object getWrapped();
    boolean hasPermission(String permission);
}
