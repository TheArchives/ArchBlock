package com.archivesmc.archblock.api;

import com.archivesmc.archblock.Plugin;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class ArchBlock {
    // Class is named this in case there are plugins using multiple protection APIs

    private Plugin plugin;

    public ArchBlock(Plugin plugin) {
        this.plugin = plugin;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public Boolean canEditBlock(Player player, Block block) {
        return true;
    }

    // TODO: Come up with a sane API
}
