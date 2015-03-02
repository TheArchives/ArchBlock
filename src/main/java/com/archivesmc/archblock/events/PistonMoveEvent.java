package com.archivesmc.archblock.events;

import com.archivesmc.archblock.Plugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

public class PistonMoveEvent implements Listener {
    private Plugin plugin;

    public PistonMoveEvent(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onExtendEvent(BlockPistonExtendEvent event) {
        // TODO: Permissions
        // TODO: WorldEdit region bypass
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onRetractEvent(BlockPistonRetractEvent event) {
        // TODO: Permissions
        // TODO: WorldEdit region bypass
    }
}
