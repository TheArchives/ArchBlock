package com.archivesmc.archblock.wrappers.sponge;

import com.archivesmc.archblock.wrappers.World;

public class SpongeWorld implements World {
    org.spongepowered.api.world.World world;

    public SpongeWorld(org.spongepowered.api.world.World world) {
        this.world = world;
    }

    @Override
    public String getName() {
        return this.world.getName();
    }

    @Override
    public Object getWrapped() {
        return this.world;
    }
}
