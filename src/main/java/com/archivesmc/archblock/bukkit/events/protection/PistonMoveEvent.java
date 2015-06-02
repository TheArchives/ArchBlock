package com.archivesmc.archblock.bukkit.events.protection;

import com.archivesmc.archblock.wrappers.bukkit.BukkitBlock;
import com.archivesmc.archblock.wrappers.bukkit.BukkitPlugin;
import org.bukkit.Effect;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

import java.util.UUID;

/**
 * Event handlers that get called when a piston extends or contracts
 */
public class PistonMoveEvent implements Listener {
    private final BukkitPlugin plugin;

    public PistonMoveEvent(BukkitPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onExtendEvent(BlockPistonExtendEvent event) {
        // Strangely enough, this also fires when sticky pistons attempt to pull blocks.
        if (this.plugin.getWorldGuardIntegration().isInIgnoredRegion(new BukkitBlock(event.getBlock()))) {
            return;
        }

        UUID owner = this.plugin.getApi().getOwnerUUID(new BukkitBlock(event.getBlock()));

        if (owner != null) {
            UUID blockOwner;

            for (Block b : event.getBlocks()) {
                blockOwner = this.plugin.getApi().getOwnerUUID(new BukkitBlock(b));

                if (blockOwner == null) {
                    continue;
                }

                if (blockOwner.equals(owner)) {
                    continue;
                }

                if (this.plugin.getApi().hasFriendship(blockOwner, owner)) {
                    continue;
                }

                event.getBlock().getWorld().playEffect(b.getLocation(), Effect.MOBSPAWNER_FLAMES, 0);

                event.getBlock().getWorld().playEffect(event.getBlock().getLocation(), Effect.SMOKE, 4);
                event.getBlock().getWorld().playEffect(event.getBlock().getLocation(), Effect.CLICK1, 0);

                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onRetractEvent(BlockPistonRetractEvent event) {
        if (this.plugin.getWorldGuardIntegration().isInIgnoredRegion(new BukkitBlock(event.getBlock()))) {
            return;
        }

        UUID owner = this.plugin.getApi().getOwnerUUID(new BukkitBlock(event.getBlock()));

        if (owner != null) {
            Block b = event.getBlock().getRelative(event.getDirection()).getRelative(event.getDirection());
            UUID blockOwner = this.plugin.getApi().getOwnerUUID(new BukkitBlock(b));

            if (blockOwner == null) {
                return;
            }

            if (blockOwner.equals(owner)) {
                return;
            }

            if (this.plugin.getApi().hasFriendship(blockOwner, owner)) {
                return;
            }

            event.getBlock().getWorld().playEffect(b.getLocation(), Effect.MOBSPAWNER_FLAMES, 0);

            event.getBlock().getWorld().playEffect(event.getBlock().getLocation(), Effect.SMOKE, 4);
            event.getBlock().getWorld().playEffect(event.getBlock().getLocation(), Effect.CLICK1, 0);

            event.setCancelled(true);
        }
    }
}
