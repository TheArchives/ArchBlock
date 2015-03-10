package com.archivesmc.archblock.events;

import com.archivesmc.archblock.Plugin;
import org.bukkit.ChatColor;
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
    private Plugin plugin;

    public PlayerInteractEvent(Plugin plugin) {
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
                UUID owner = this.plugin.getApi().getOwnerUUID(block);

                if (owner == null) {
                    event.getPlayer().sendMessage(String.format(
                            "%s[%sArchBlock%s]%s This block is not owned by anyone.",
                            ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE,
                            ChatColor.AQUA
                    ));
                } else {
                    event.getPlayer().sendMessage(String.format(
                            "%s[%sArchBlock%s]%s This block is owned by %s%s%s.",
                            ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE,
                            ChatColor.AQUA, ChatColor.AQUA, this.plugin.getApi().getUsernameForUuid(owner),
                            ChatColor.RED
                    ));
                }
            }
        }
    }
}
