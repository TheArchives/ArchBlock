package com.archivesmc.archblock.events.protection.special;

import com.archivesmc.archblock.Plugin;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class BlockFormEvent implements Listener {
    private final Plugin plugin;

    public BlockFormEvent(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEvent(org.bukkit.event.block.BlockFormEvent event) {
        Block target = event.getBlock().getRelative(BlockFace.DOWN);

        if (this.plugin.getWorldGuardIntegration().isInIgnoredRegion(target)) {
            return;
        }

        if (this.plugin.getApi().hasOwner(target)) {
            event.setCancelled(true);
        }
    }
}
