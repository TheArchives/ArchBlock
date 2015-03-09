package com.archivesmc.archblock.commands;

import com.archivesmc.archblock.Plugin;
import com.archivesmc.archblock.storage.entities.Friendship;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FriendsCommand implements CommandExecutor {
    private Plugin plugin;

    public FriendsCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // TODO: Staff overrides

        String toCheck;

        if (sender instanceof Player) {
            toCheck = sender.getName();
        } else {
            if (args.length < 1) {
                sender.sendMessage(String.format(
                        "%s[%sArchBlock%s]%s Usage: %s/%s%s %s<user>",
                        ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE,
                        ChatColor.BLUE, ChatColor.AQUA, ChatColor.DARK_AQUA,
                        label, ChatColor.DARK_GREEN
                ));

                return true;
            } else {
                toCheck = args[0];
            }
        }

        String uuid = this.plugin.getApi().getUuidForUsername(toCheck).toString();

        if (uuid == null) {
            sender.sendMessage(String.format(
                    "%s[%sArchBlock%s]%s Unknown player: %s%s",
                    ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE,
                    ChatColor.RED, ChatColor.AQUA, args[0]
            ));
        } else {
            List friends = this.plugin.getApi().getFriendships(UUID.fromString(uuid));

            if (friends.size() < 1) {
                sender.sendMessage(String.format(
                        "%s[%sArchBlock%s]%s No friends found :(",
                        ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE,
                        ChatColor.RED
                ));

                return true;
            }

            List<List<String>> lines = new ArrayList<>();
            List<String> currentLine = new ArrayList<>();
            Map p;

            for (Object o : friends) {
                p = (Map) o;

                if (currentLine.size() == 5) {
                    lines.add(currentLine);
                    currentLine = new ArrayList<>();
                }

                currentLine.add(
                        ((com.archivesmc.archblock.storage.entities.Player) p.get("this")).getUsername()
                );
            }

            if (currentLine.size() > 0) {
                lines.add(currentLine);
            }

            sender.sendMessage(String.format(
                    "== %s[%sFriends%s]%s ==",
                    ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE,
                    ChatColor.WHITE
            ));

            for (List<String> line : lines) {
                sender.sendMessage(String.format(
                        "%s%s", ChatColor.AQUA,
                        StringUtils.join(line, String.format(
                                "%s, %s", ChatColor.DARK_AQUA, ChatColor.AQUA
                        ))
                ));
            }
        }

        return true;
    }
}
