package com.archivesmc.archblock.commands;

import com.archivesmc.archblock.Plugin;
import com.archivesmc.archblock.runnables.database.commands.MassOwnershipChanger;
import com.archivesmc.archblock.runnables.RelayRunnable;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

/**
 * The command handler for the /setowner command for setting the owner
 * of a large number of blocks, contained within a WorldEdit selection
 */
public class SetOwnerCommand implements CommandExecutor {
    private final Plugin plugin;

    public SetOwnerCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("archblock.admin")) {
            sender.sendMessage(this.plugin.getPrefixedLocalisedString("command_no_permission"));
        } else {
            if (args.length < 1) {
                sender.sendMessage(this.plugin.getPrefixedLocalisedString("setowner_command_usage", label));
                sender.sendMessage(this.plugin.getPrefixedLocalisedString("setowner_command_usage_exclamation_point"));
            } else if (sender instanceof Player) {
                if (this.plugin.isTaskRunning()) {
                    sender.sendMessage(this.plugin.getPrefixedLocalisedString("task_already_running"));
                    return true;
                }

                UUID target = this.plugin.getApi().getUuidForUsername(args[0]);

                if (target == null && !("!".equals(args[0]))) {
                    sender.sendMessage(this.plugin.getPrefixedLocalisedString("unknown_player", args[0]));
                } else {
                    Selection selection = this.plugin.getWorldEdit().getSelection((Player) sender);

                    if (selection instanceof CuboidSelection) {
                        CuboidSelection cs = (CuboidSelection) selection;

                        ArrayList<Block> blocks = new ArrayList<>();

                        Location min = cs.getMinimumPoint();
                        Location max = cs.getMaximumPoint();

                        int minX = min.getBlockX();
                        int minY = min.getBlockY();
                        int minZ = min.getBlockZ();

                        int maxX = max.getBlockX();
                        int maxY = max.getBlockY();
                        int maxZ = max.getBlockZ();

                        int i, j, k;

                        for (i = minX; i <= maxX; i += 1) {
                            for (j = minY; j <= maxY; j += 1) {
                                for (k = minZ; k <= maxZ; k += 1) {
                                    Block b = ((Player) sender).getWorld().getBlockAt(i, j, k);
                                    if (b.getType() != Material.AIR) {
                                        blocks.add(b);
                                    }
                                }
                            }
                        }

                        RelayRunnable finishRunnable = new RelayRunnable(this.plugin, sender.getName(), this.plugin.getPrefixedLocalisedString(
                                "ownership_blocks_changed", blocks.size()
                        ));

                        MassOwnershipChanger changer = new MassOwnershipChanger(
                                this.plugin, blocks, target, finishRunnable
                        );

                        changer.start();

                        sender.sendMessage(this.plugin.getPrefixedLocalisedString("setowner_command_changing_ownership", blocks.size()));
                    } else {
                        sender.sendMessage(this.plugin.getPrefixedLocalisedString("setowner_command_make_worldedit_selection"));
                    }
                }
            } else {
                sender.sendMessage(this.plugin.getPrefixedLocalisedString("player_only"));
            }
        }

        return true;
    }
}
