package com.archivesmc.archblock.commands;

import com.archivesmc.archblock.Plugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * The command handler for the /friend command for adding friends to
 * one's friends list.
 */
public class FriendCommand implements CommandExecutor {
    private final Plugin plugin;

    public FriendCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(String.format(
                    "%s[%sSncProtect%s]%s gebruik: %s/%s%s %s<user>",
                    ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE,
                    ChatColor.BLUE, ChatColor.AQUA, ChatColor.DARK_AQUA,
                    label, ChatColor.DARK_GREEN
            ));
        } else {
            UUID player;
            UUID friend;

            if (sender instanceof Player) {
                player = ((Player) sender).getUniqueId();
                friend = this.plugin.getApi().getUuidForUsername(args[0]);

                if (friend == null) {
                    sender.sendMessage(String.format(
                            "%s[%sSncProtect%s]%s ombekende speler: %s%s",
                            ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE,
                            ChatColor.RED, ChatColor.AQUA, args[0]
                    ));
                } else {
                    if (this.plugin.getApi().hasFriendship(player, friend)) {
                        sender.sendMessage(String.format(
                                "%s[%sSncProtect%s]%s Je hebt hem/haar al geballowt: %s%s",
                                ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE,
                                ChatColor.RED, ChatColor.AQUA, args[0]
                        ));
                    } else {
                        this.plugin.getApi().createFriendship(player, friend);
                        sender.sendMessage(String.format(
                                "%s[%sSncProtect%s]%s je hebt nu %s%s",
                                ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE,
                                ChatColor.GREEN, ChatColor.AQUA, args[0]
                        ));
                    }
                }
            } else {
                if (args.length < 2) {
                    sender.sendMessage(String.format(
                            "%s[%sSncProtect%s]%s Usage: %s/%s%s %s<user> <friend>",
                            ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE,
                            ChatColor.BLUE, ChatColor.AQUA, ChatColor.DARK_AQUA,
                            label, ChatColor.DARK_GREEN
                    ));
                } else {
                    player = this.plugin.getApi().getUuidForUsername(args[0]);
                    friend = this.plugin.getApi().getUuidForUsername(args[1]);

                    if (player == null) {
                        sender.sendMessage(String.format(
                                "%s[%sSncProtect%s]%s Unknown player: %s%s",
                                ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE,
                                ChatColor.RED, ChatColor.AQUA, args[0]
                        ));
                    } else if (friend == null) {
                        sender.sendMessage(String.format(
                                "%s[%sSncProtect%s]%s Unknown player: %s%s Geballowd.",
                                ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE,
                                ChatColor.RED, ChatColor.AQUA, args[1]
                        ));
                    } else {
                        if (this.plugin.getApi().hasFriendship(player, friend)) {
                            sender.sendMessage(String.format(
                                    "%s[%sSncProtect%s]%s %s%s heeft al %s%s Geballowd.",
                                    ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE,
                                    ChatColor.AQUA, args[0], ChatColor.RED, ChatColor.AQUA, args[1]
                            ));
                        } else {
                            this.plugin.getApi().createFriendship(player, friend);

                            sender.sendMessage(String.format(
                                    "%s[%sSncProtect%s]%s %s%s ballowedt %s%s",
                                    ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE,
                                    ChatColor.AQUA, args[0], ChatColor.GREEN, ChatColor.AQUA, args[1]
                            ));
                        }
                    }
                }
            }
        }

        return true;
    }
}
