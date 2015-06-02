package com.archivesmc.archblock.bukkit.events.protection;

import com.archivesmc.archblock.wrappers.bukkit.BukkitBlock;
import com.archivesmc.archblock.wrappers.bukkit.BukkitPlayer;
import com.archivesmc.archblock.wrappers.bukkit.BukkitPlugin;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * Event handler that gets called when a player left- or right-clicks a block
 */
public class PlayerInteractEvent implements Listener {
    private BukkitPlugin plugin;

    public PlayerInteractEvent(BukkitPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = false)
    public void onEvent(org.bukkit.event.player.PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        Action action = event.getAction();

        if (block == null) {
            return;
        }

        ItemStack itemStack = event.getPlayer().getItemInHand();

        if (itemStack == null) {
            return;
        }

        if (event.getPlayer().getItemInHand().getType().equals(Material.WOOD_SWORD)) {

            if (action.equals(Action.LEFT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
                UUID owner = this.plugin.getApi().getOwnerUUID(new BukkitBlock(block));

                if (owner == null) {
                    event.getPlayer().sendMessage(this.plugin.getPrefixedLocalisedString("event_interact_sword_not_owned"));
                } else {
                    event.getPlayer().sendMessage(this.plugin.getPrefixedLocalisedString(
                            "event_interact_sword_owned_by", this.plugin.getApi().getUsernameForUuid(owner)
                    ));
                }

                event.setCancelled(true);
            }
        } else {
            if (this.plugin.getInteractProtected().contains(event.getClickedBlock().getType().toString())) {
                if (!this.plugin.getApi().canEditBlock(new BukkitBlock(event.getClickedBlock()), new BukkitPlayer(event.getPlayer()))) {
                    event.getPlayer().sendMessage(this.plugin.getPrefixedLocalisedString(
                            "event_interact_denied", event.getClickedBlock().getType().toString(),
                            this.plugin.getApi().getUsernameForUuid(this.plugin.getApi().getOwnerUUID(new BukkitBlock(event.getClickedBlock())))
                    ));

                    event.setCancelled(true);
                }
            }
        }
    }
}
