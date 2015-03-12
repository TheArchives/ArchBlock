package com.archivesmc.archblock.events.protection.special;

import com.archivesmc.archblock.Plugin;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.UUID;

public class BlockFromToEventLiquids implements Listener {
    private final Plugin plugin;

    public BlockFromToEventLiquids(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEvent(org.bukkit.event.block.BlockFromToEvent event) {
        if (this.plugin.getWorldGuardIntegration().isInIgnoredRegion(event.getToBlock())) {
            return;
        }

        if (this.plugin.getApi().hasOwner(event.getToBlock())) {
            event.setCancelled(true);
        }
    }
}
