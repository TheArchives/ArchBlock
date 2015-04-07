package com.archivesmc.archblock.commands;

import com.archivesmc.archblock.Plugin;
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

/**
 * The command handler for the /friends command for viewing the contents
 * of one's friends list
 */
public class FriendsCommand implements CommandExecutor {
    private final Plugin plugin;

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
                sender.sendMessage(this.plugin.getPrefixedLocalisedString("friends_command_console_usage", label));
                return true;
            } else {
                toCheck = args[0];
            }
        }

        UUID uuid = this.plugin.getApi().getUuidForUsername(toCheck);

        if (uuid == null) {
            sender.sendMessage(this.plugin.getPrefixedLocalisedString("unknown_player", args[0]));
        } else {
            List friends = this.plugin.getApi().getFriendships(uuid);

            if (friends.size() < 1) {
                sender.sendMessage(this.plugin.getPrefixedLocalisedString("friends_command_no_friends"));
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

            sender.sendMessage(this.plugin.getLocalisedString("friends_command_list_header"));

            for (List<String> line : lines) {
                sender.sendMessage(
                        this.plugin.getLocalisedString("friends_command_list_row",
                                StringUtils.join(
                                        line, this.plugin.getLocalisedString("friends_command_list_separator")
                                )
                        )
                );
            }
        }

        return true;
    }
}
