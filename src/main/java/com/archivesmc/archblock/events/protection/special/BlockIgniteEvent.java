package com.archivesmc.archblock.events.protection.special;

import com.archivesmc.archblock.Plugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class BlockIgniteEvent implements Listener {
    private final Plugin plugin;

    public BlockIgniteEvent(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEvent(org.bukkit.event.block.BlockIgniteEvent event) {
        if (this.plugin.getWorldGuardIntegration().isInIgnoredRegion(event.getBlock())) {
            return;
        }

        if (this.plugin.getApi().hasOwner(event.getBlock())) {
            event.setCancelled(true);
        }
    }
}
