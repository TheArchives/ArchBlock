package com.archivesmc.archblock.commands;

import com.archivesmc.archblock.Plugin;
import com.archivesmc.archblock.runnables.RelayRunnable;
import com.archivesmc.archblock.runnables.database.commands.TransferBlocksThread;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TransferBlocksCommand implements CommandExecutor {
    private final Plugin plugin;

    public TransferBlocksCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("archblock.transfer")) {
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
            } else {
                UUID uuid = this.plugin.getApi().getUuidForUsername(args[0]);

                if (uuid == null) {
                    sender.sendMessage(String.format(
                            "%s[%sArchBlock%s]%s Unknown player: %s%s",
                            ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE,
                            ChatColor.RED, ChatColor.AQUA, args[0]
                    ));
                } else {
                    RelayRunnable callback;

                    if (sender instanceof Player) {
                        callback = new RelayRunnable(this.plugin, sender.getName());
                    } else {
                        sender.sendMessage("This command may only be run by a player.");
                        return true;
                    }

                    new TransferBlocksThread(this.plugin, ((Player) sender).getUniqueId(), uuid, callback).start();

                    sender.sendMessage(
                            String.format(
                                    "%s[%sArchBlock%s]%s Transferring your blocks to %s%s%s. This may take a while.",
                                    ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE,
                                    ChatColor.BLUE, ChatColor.AQUA, args[0], ChatColor.BLUE
                            )
                    );
                }

                if (this.plugin.isTaskRunning()) {
                    sender.sendMessage(String.format(
                            "%s[%sArchBlock%s]%s There is already a task running. Please wait for it to finish.",
                            ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE,
                            ChatColor.RED
                    ));
                    return true;
                }
            }
        }

        return true;
    }
}
