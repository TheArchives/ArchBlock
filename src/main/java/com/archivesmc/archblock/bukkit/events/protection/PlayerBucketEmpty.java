package com.archivesmc.archblock.bukkit.events.protection;

import com.archivesmc.archblock.wrappers.bukkit.BukkitBlock;
import com.archivesmc.archblock.wrappers.bukkit.BukkitPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

import java.util.UUID;

public class PlayerBucketEmpty implements Listener {
    private final BukkitPlugin plugin;

    public PlayerBucketEmpty(BukkitPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEvent(PlayerBucketEmptyEvent event) {
        UUID owner = this.plugin.getApi().getOwnerUUID(new BukkitBlock(event.getBlockClicked()));
        UUID ourUuid = event.getPlayer().getUniqueId();

        if (this.plugin.getWorldGuardIntegration().isInIgnoredRegion(new BukkitBlock(event.getBlockClicked()))) {
            this.plugin.debug("Region has bypass-protection set to true");
            return;
        }


        if (owner != null && ! owner.equals(ourUuid)) {
            if (! event.getPlayer().hasPermission("archblock.bypass") && ! this.plugin.getApi().hasFriendship(owner, ourUuid)) {
                event.getPlayer().sendMessage(this.plugin.getPrefixedLocalisedString(
                        "event_bucket_denied", this.plugin.getApi().getUsernameForUuid(owner)
                ));

                event.setCancelled(true);
                return;
            }
            this.plugin.debug("Owner is different but player has a bypass or is friends with the owner");
        }

        this.plugin.debug("Everything's okay - empty ze bucket");
        this.plugin.getApi().removeOwner(new BukkitBlock(event.getBlockClicked()));
    }
}
