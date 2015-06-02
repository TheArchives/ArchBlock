package com.archivesmc.archblock.wrappers.sponge;

import com.archivesmc.archblock.wrappers.Block;
import com.archivesmc.archblock.wrappers.World;

public class SpongeBlock implements Block {
    @Override
    public World getWorld() {
        return null;
    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public int getZ() {
        return 0;
    }

    @Override
    public Object getWrapped() {
        return null;
    }
}
