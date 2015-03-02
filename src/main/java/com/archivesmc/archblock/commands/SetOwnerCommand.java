package com.archivesmc.archblock.commands;

import com.archivesmc.archblock.Plugin;
import com.archivesmc.archblock.runnables.MassOwnershipChanger;
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

public class SetOwnerCommand implements CommandExecutor {
    private Plugin plugin;

    public SetOwnerCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // TODO: WorldEdit support
        // TODO: Transfer blocks between users, and also in a WorldEdit selection

        if (!sender.hasPermission("archblock.admin")) {
            sender.sendMessage(String.format(
                    "%s[%sArchBlock%s]%s You do not have permission to access this command.",
                    ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE, ChatColor.RED
            ));
        } else {
            if (args.length < 1) {
                sender.sendMessage(String.format(
                    "%s[%sArchBlock%s]%s Usage: %s/%s%s %s<user>",
                    ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE,
                    ChatColor.BLUE, ChatColor.AQUA, ChatColor.DARK_AQUA,
                        label, ChatColor.DARK_GREEN
                ));
                sender.sendMessage(String.format(
                        "%s[%sArchBlock%s]%s Supply an exclamation point %s(\"!\")%s instead of a username to disown the selected blocks instead.",
                        ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE, ChatColor.BLUE, ChatColor.AQUA, ChatColor.BLUE
                ));
            } else if (sender instanceof Player) {
                UUID target = this.plugin.getApi().getUuidForUsername(args[0]);

                if (target == null && !("!".equals(args[0]))) {
                    sender.sendMessage(String.format(
                            "%s[%sArchBlock%s]%s Unknown player: %s%s",
                            ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE,
                            ChatColor.RED, ChatColor.AQUA, args[0]
                    ));
                } else {
                    Selection selection = this.plugin.getWorldEdit().getSelection((Player) sender);

                    if (selection instanceof CuboidSelection) {
                        CuboidSelection cs = (CuboidSelection) selection;

                        ArrayList<Block> blocks = new ArrayList<>();

                        Location min = cs.getMinimumPoint();
                        Location max = cs.getMaximumPoint();

                        Integer minX = min.getBlockX();
                        Integer minY = min.getBlockY();
                        Integer minZ = min.getBlockZ();

                        Integer maxX = max.getBlockX();
                        Integer maxY = max.getBlockY();
                        Integer maxZ = max.getBlockZ();

                        int i, j, k;

                        for (i = minX; i <= maxX; i += 1) {
                            for (j = minY; i <= maxY; i += 1) {
                                for (k = minZ; i <= maxZ; i += 1) {
                                    Block b = ((Player) sender).getWorld().getBlockAt(i, j, k);
                                    if (b.getType() != Material.AIR) {
                                        blocks.add(b);
                                    }
                                }
                            }
                        }

                        RelayRunnable finishRunnable = new RelayRunnable(this.plugin, sender.getName(), String.format(
                                "%s[%sArchBlock%s]%s Ownership of %s%s%s blocks has been changed.",
                                ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE, ChatColor.BLUE,
                                ChatColor.RED, blocks.size(), ChatColor.BLUE
                        ));

                        MassOwnershipChanger changer = new MassOwnershipChanger(
                                this.plugin, blocks, target, finishRunnable
                        );

                        changer.start();

                        sender.sendMessage(String.format(
                                "%s[%sArchBlock%s]%s Changing ownership of %s%s%s blocks. This may take a while.",
                                ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE, ChatColor.BLUE,
                                ChatColor.RED, blocks.size(), ChatColor.BLUE
                        ));
                    } else {
                        sender.sendMessage(String.format(
                                "%s[%sArchBlock%s]%s Please make a valid WorldEdit cuboid selection.",
                                ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE, ChatColor.RED
                        ));
                    }
                }
            } else {
                sender.sendMessage(String.format(
                        "%s[%sArchBlock%s]%s This command may only be run by a player.",
                        ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE, ChatColor.RED
                ));
            }
        }

        return true;
    }
}
