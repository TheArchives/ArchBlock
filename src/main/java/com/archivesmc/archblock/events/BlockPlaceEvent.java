package com.archivesmc.archblock.events;

import com.archivesmc.archblock.Plugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class BlockPlaceEvent implements Listener {
    private Plugin plugin;

    public BlockPlaceEvent(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEvent(org.bukkit.event.block.BlockPlaceEvent event) {

    }
}
