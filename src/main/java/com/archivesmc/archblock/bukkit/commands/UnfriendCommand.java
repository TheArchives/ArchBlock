package com.archivesmc.archblock.bukkit.commands;

import com.archivesmc.archblock.wrappers.bukkit.BukkitPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * The command handler for the /unfriend command for removing friends from
 * one's friends list.
 */
public class UnfriendCommand implements CommandExecutor {
    private final BukkitPlugin plugin;

    public UnfriendCommand(BukkitPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(this.plugin.getPrefixedLocalisedString("unfriend_command_usage", label));
        } else {
            UUID player;
            UUID friend;

            if (sender instanceof Player) {
                player = ((Player) sender).getUniqueId();
                friend = this.plugin.getApi().getUuidForUsername(args[0]);

                if (friend == null) {
                    sender.sendMessage(this.plugin.getPrefixedLocalisedString("unknown_player", args[0]));
                } else {
                    if (! this.plugin.getApi().hasFriendship(player, friend)) {
                        sender.sendMessage(
                                this.plugin.getPrefixedLocalisedString("unfriend_command_not_friends", args[0])
                        );
                    } else {
                        this.plugin.getApi().destroyFriendship(player, friend);

                        sender.sendMessage(
                                this.plugin.getPrefixedLocalisedString("unfriend_command_no_longer_friends", args[0])
                        );
                    }
                }
            } else {
                if (args.length < 2) {
                    sender.sendMessage(this.plugin.getPrefixedLocalisedString("unfriend_command_console_usage", label));
                } else {
                    player = this.plugin.getApi().getUuidForUsername(args[0]);
                    friend = this.plugin.getApi().getUuidForUsername(args[1]);

                    if (player == null) {
                        sender.sendMessage(this.plugin.getPrefixedLocalisedString("unknown_player", args[0]));
                    } else if (friend == null) {
                        sender.sendMessage(this.plugin.getPrefixedLocalisedString("unknown_player", args[1]));
                    } else {
                        if (! this.plugin.getApi().hasFriendship(player, friend)) {
                            sender.sendMessage(
                                    this.plugin.getPrefixedLocalisedString("unfriend_command_console_not_friends", args[0], args[1])
                            );
                        } else {
                            this.plugin.getApi().destroyFriendship(player, friend);

                            sender.sendMessage(
                                    this.plugin.getPrefixedLocalisedString("unfriend_command_console_no_longer_friends", args[0], args[1])
                            );
                        }
                    }
                }
            }
        }

        return true;
    }
}
