package com.archivesmc.archblock.wrappers.bukkit;

import com.archivesmc.archblock.wrappers.World;

public class BukkitWorld implements World {
    private org.bukkit.World wrappedWorld;

    public BukkitWorld(org.bukkit.World wrappedWorld) {
        this.wrappedWorld = wrappedWorld;
    }

    @Override
    public String getName() {
        return this.wrappedWorld.getName();
    }

    @Override
    public Object getWrapped() {
        return this.wrappedWorld;
    }
}
