package com.archivesmc.archblock.commands;

import com.archivesmc.archblock.Plugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class FriendCommand implements CommandExecutor {
    private Plugin plugin;

    public FriendCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // TODO: Friends list adding
        // TODO: Staff overrides

        if (args.length < 1) {
            sender.sendMessage(String.format(
                    "%s[%sWatchBlock%s]%s Usage: %s/%s%s %s<user>",
                    ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE,
                    ChatColor.BLUE, ChatColor.AQUA, ChatColor.DARK_AQUA,
                    command, ChatColor.DARK_GREEN
            ));
        } else {
            sender.sendMessage(String.format(
                    "%s[%sWatchBlock%s]%s This command hasn't been implemented yet.",
                    ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE, ChatColor.RED
            ));
        }

        return true;
    }
}
