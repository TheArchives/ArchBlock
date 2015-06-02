package com.archivesmc.archblock.wrappers;

public interface Block {
    World getWorld();
    int getX();
    int getY();
    int getZ();

    Object getWrapped();
}
