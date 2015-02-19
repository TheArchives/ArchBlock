package com.archivesmc.archblock.api;

import com.archivesmc.archblock.Plugin;

public class ArchBlock {
    // Class is named this in case there are plugins using multiple protection APIs

    private Plugin plugin;

    public ArchBlock(Plugin plugin) {
        this.plugin = plugin;
    }

    // TODO: Come up with a sane API
}
