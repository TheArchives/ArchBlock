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
        if (!sender.hasPermission("marfprotect.admin")) {
            sender.sendMessage(String.format(
                    "%s[%sSncProtect%s]%s Je hebt geen toegang tot die commando.",
                    ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE, ChatColor.RED
            ));
        } else {
            if (args.length < 1) {
                sender.sendMessage(String.format(
                    "%s[%sSncProtect%s]%s Gebruik: %s/%s%s %s<user>",
                    ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE,
                    ChatColor.BLUE, ChatColor.AQUA, ChatColor.DARK_AQUA,
                        label, ChatColor.DARK_GREEN
                ));
                sender.sendMessage(String.format(
                        "%s[%sSncProtect%s]%s Zet er een uitroepteken %s(\"!\")%s in plaats van een gebruikersnaam om de geselecteerde blokken in plaats daarvan verloochenen.",
                        ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE, ChatColor.BLUE, ChatColor.AQUA, ChatColor.BLUE
                ));
            } else if (sender instanceof Player) {
                if (this.plugin.isTaskRunning()) {
                    sender.sendMessage(String.format(
                            "%s[%sSncProtect%s]%s Er is al een taak bezig. Een ogenblick geduld aub.",
                            ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE,
                            ChatColor.RED
                    ));
                    return true;
                }

                UUID target = this.plugin.getApi().getUuidForUsername(args[0]);

                if (target == null && !("!".equals(args[0]))) {
                    sender.sendMessage(String.format(
                            "%s[%sSncProtect%s]%s Ombekende speler: %s%s",
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

                        RelayRunnable finishRunnable = new RelayRunnable(this.plugin, sender.getName(), String.format(
                                "%s[%sSncProtect%s]%s Eigenaarschap blockken van %s%s%s is nu veranderd.",
                                ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE, ChatColor.BLUE,
                                ChatColor.RED, blocks.size(), ChatColor.BLUE
                        ));

                        MassOwnershipChanger changer = new MassOwnershipChanger(
                                this.plugin, blocks, target, finishRunnable
                        );

                        changer.start();

                        sender.sendMessage(String.format(
                                "%s[%sSncProtect%s]%s Verandert Eigenaarschap van blokken %s%s%s Dit kan even duren.",
                                ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE, ChatColor.BLUE,
                                ChatColor.RED, blocks.size(), ChatColor.BLUE
                        ));
                    } else {
                        sender.sendMessage(String.format(
                                "%s[%sSncProtect%s]%s Maak een geldig Worldedit cuboid selectie.",
                                ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE, ChatColor.RED
                        ));
                    }
                }
            } else {
                sender.sendMessage(String.format(
                        "%s[%sSncProtect%s]%s This command may only be run by a player.",
                        ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE, ChatColor.RED
                ));
            }
        }

        return true;
    }
}
