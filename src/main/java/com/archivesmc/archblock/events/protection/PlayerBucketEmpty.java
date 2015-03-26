package com.archivesmc.archblock.events.protection;

import com.archivesmc.archblock.Plugin;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

import java.util.UUID;

public class PlayerBucketEmpty implements Listener {
    private final Plugin plugin;

    public PlayerBucketEmpty(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEvent(PlayerBucketEmptyEvent event) {
        UUID owner = this.plugin.getApi().getOwnerUUID(event.getBlockClicked());
        UUID ourUuid = event.getPlayer().getUniqueId();

        if (this.plugin.getWorldGuardIntegration().isInIgnoredRegion(event.getBlockClicked())) {
            this.plugin.debug("Region has bypass-protection set to true");
            return;
        }


        if (owner != null && ! owner.equals(ourUuid)) {
            if (! event.getPlayer().hasPermission("marfprotect.bypass") && ! this.plugin.getApi().hasFriendship(owner, ourUuid)) {
                event.getPlayer().sendMessage(
                        String.format(
                                "%s[%sSncProtect%s]%s Je mag geen emmers legen op blockken van: %s%s%s.",
                                ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE,
                                ChatColor.RED, ChatColor.AQUA, this.plugin.getApi().getUsernameForUuid(owner),
                                ChatColor.RED
                        )
                );

                event.setCancelled(true);
                return;
            }
            this.plugin.debug("Owner is different but player has a bypass or is friends with the owner");
        }

        this.plugin.debug("Everything's okay - empty ze bucket");
        this.plugin.getApi().removeOwner(event.getBlockClicked());
    }
}
