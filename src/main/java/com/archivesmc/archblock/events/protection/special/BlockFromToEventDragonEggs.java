package com.archivesmc.archblock.events.protection.special;

import com.archivesmc.archblock.Plugin;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.UUID;

public class BlockFromToEventDragonEggs implements Listener {
    private final Plugin plugin;

    public BlockFromToEventDragonEggs(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEvent(org.bukkit.event.block.BlockFromToEvent event) {
        if (this.plugin.getWorldGuardIntegration().isInIgnoredRegion(event.getToBlock())) {
            return;
        }

        if (event.getBlock().getType().equals(Material.DRAGON_EGG)) {
            UUID targetOwner = this.plugin.getApi().getOwnerUUID(event.getToBlock().getRelative(BlockFace.DOWN));

            if (targetOwner == null) {
                return;
            }

            UUID eggOwner = this.plugin.getApi().getOwnerUUID(event.getBlock());

            if (eggOwner == null) {
                event.setCancelled(true);
                return;
            }

            if (targetOwner.equals(eggOwner)) {
                return;
            }

            if (! this.plugin.getApi().hasFriendship(targetOwner, eggOwner)) {
                event.setCancelled(true);
            }
        }
    }
}
