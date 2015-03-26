package com.archivesmc.archblock.events.protection.special;

import com.archivesmc.archblock.Plugin;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.UUID;

public class BlockDamageEvent implements Listener {
    private final Plugin plugin;

    public BlockDamageEvent(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEvent(org.bukkit.event.block.BlockDamageEvent event) {
        if (this.plugin.getWorldGuardIntegration().isInIgnoredRegion(event.getBlock())) {
            return;
        }

        if (event.getPlayer().hasPermission("marfprotect.bypass")) {
            return;
        }

        UUID owner = this.plugin.getApi().getOwnerUUID(event.getBlock());

        if (owner == null) {
            return;
        }

        if (owner.equals(event.getPlayer().getUniqueId())) {
            return;
        }

        if (this.plugin.getApi().hasFriendship(owner, event.getPlayer().getUniqueId())) {
            return;
        }

        event.getPlayer().sendMessage(String.format(
                "%s[%sSncProtect%s]%s Deze block is niet van jou. hij is van: %s%s%s.",
                ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE,
                ChatColor.RED, ChatColor.AQUA, this.plugin.getApi().getUsernameForUuid(owner),
                ChatColor.RED
        ));

        event.setCancelled(true);
    }
}
