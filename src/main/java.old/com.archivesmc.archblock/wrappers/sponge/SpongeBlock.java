package com.archivesmc.archblock.wrappers.sponge;

import com.archivesmc.archblock.wrappers.Block;
import com.archivesmc.archblock.wrappers.World;
import org.spongepowered.api.world.Location;

public class SpongeBlock implements Block {
    Location block;

    public SpongeBlock(Location block) {
        this.block = block;
    }

    @Override
    public World getWorld() {
        if (this.block.getExtent() instanceof org.spongepowered.api.world.World) {
            return new SpongeWorld((org.spongepowered.api.world.World) this.block.getExtent());
        } else {
            return null;
        }
    }

    @Override
    public int getX() {
        return this.block.getBlockX();
    }

    @Override
    public int getY() {
        return this.block.getBlockY();
    }

    @Override
    public int getZ() {
        return this.block.getBlockZ();
    }

    @Override
    public Object getWrapped() {
        return this.block;
    }
}
