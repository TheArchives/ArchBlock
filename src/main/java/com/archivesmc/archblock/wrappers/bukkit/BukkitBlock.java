package com.archivesmc.archblock.wrappers.bukkit;

import com.archivesmc.archblock.wrappers.Block;
import com.archivesmc.archblock.wrappers.World;

public class BukkitBlock implements Block {
    private org.bukkit.block.Block wrappedBlock;

    public BukkitBlock(org.bukkit.block.Block wrappedBlock) {
        this.wrappedBlock = wrappedBlock;
    }

    @Override
    public World getWorld() {
        return new BukkitWorld(this.wrappedBlock.getWorld());
    }

    @Override
    public int getX() {
        return this.wrappedBlock.getX();
    }

    @Override
    public int getY() {
        return this.wrappedBlock.getY();
    }

    @Override
    public int getZ() {
        return this.wrappedBlock.getZ();
    }

    @Override
    public Object getWrapped() {
        return this.wrappedBlock;
    }
}
