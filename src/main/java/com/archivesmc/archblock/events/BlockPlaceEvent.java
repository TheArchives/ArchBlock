package com.archivesmc.archblock.events;

import com.archivesmc.archblock.Plugin;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.UUID;

public class BlockPlaceEvent implements Listener {
    private Plugin plugin;

    public BlockPlaceEvent(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEvent(org.bukkit.event.block.BlockPlaceEvent event) {
        UUID owner = this.plugin.getApi().getOwnerUUID(event.getBlockAgainst());
        UUID ourUuid = event.getPlayer().getUniqueId();

        if (owner == null) {
            return;
        }

        if (!event.getPlayer().hasPermission("archblock.bypass")) {
            return;
        }

        if (this.plugin.getWorldGuardIntegration().isInIgnoredRegion(event.getBlock())) {
            return;
        }

        if (! owner.equals(ourUuid)) {
            if (! this.plugin.getApi().hasFriendship(owner, ourUuid)) {
                event.getPlayer().sendMessage(
                        String.format(
                                "%s[%sArchBlock%s]%s You may not place blocks against those owned by %s%s%s.",
                                ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE,
                                ChatColor.RED, ChatColor.AQUA, this.plugin.getApi().getUsernameForUuid(owner),
                                ChatColor.RED
                        )
                );

                event.setCancelled(true);
                return;
            }
        }

        this.plugin.getApi().setOwnerUUID(event.getBlock(), ourUuid);
    }
}
