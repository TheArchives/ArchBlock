package com.archivesmc.archblock.events;

import com.archivesmc.archblock.Plugin;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.UUID;

public class BlockBreakEvent implements Listener {
    private Plugin plugin;

    public BlockBreakEvent(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEvent(org.bukkit.event.block.BlockBreakEvent event) {
        UUID owner = this.plugin.getApi().getOwnerUUID(event.getBlock());
        UUID ourUuid = event.getPlayer().getUniqueId();

        if (owner == null) {
            return;
        }

        if (event.getPlayer().hasPermission("archblock.bypass")) {
            return;
        }

        if (this.plugin.getWorldGuardIntegration().isInIgnoredRegion(event.getBlock())) {
            return;
        }


        if (! owner.equals(ourUuid)) {
            if (! this.plugin.getApi().hasFriendship(owner, ourUuid)) {
                event.getPlayer().sendMessage(
                        String.format(
                                "%s[%sArchBlock%s]%s You may not break blocks owned by %s%s%s.",
                                ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE,
                                ChatColor.RED, ChatColor.AQUA, this.plugin.getApi().getUsernameForUuid(owner),
                                ChatColor.RED
                        )
                );

                event.setCancelled(true);
                return;
            }
        }


        this.plugin.getApi().removeOwner(event.getBlock());
    }
}
