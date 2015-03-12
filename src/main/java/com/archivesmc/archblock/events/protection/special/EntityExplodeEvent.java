package com.archivesmc.archblock.events.protection.special;

import com.archivesmc.archblock.Plugin;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class EntityExplodeEvent implements Listener {
    private final Plugin plugin;

    public EntityExplodeEvent(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEvent(org.bukkit.event.entity.EntityExplodeEvent event) {
        Block target = event.getEntity().getLocation().getBlock().getRelative(BlockFace.DOWN);
        if (this.plugin.getWorldGuardIntegration().isInIgnoredRegion(target)) {
            return;
        }

        if (this.plugin.getApi().hasOwner(target)) {
            event.setCancelled(true);
            event.getEntity().remove();
        }
    }
}
