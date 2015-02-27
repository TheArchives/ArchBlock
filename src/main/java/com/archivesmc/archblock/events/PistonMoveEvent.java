package com.archivesmc.archblock.events;

import com.archivesmc.archblock.Plugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonEvent;

public class PistonMoveEvent implements Listener {
    private Plugin plugin;

    public PistonMoveEvent(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEvent(BlockPistonEvent event) {
        // TODO: Permissions
        // TODO: WorldEdit region bypass
    }
}
