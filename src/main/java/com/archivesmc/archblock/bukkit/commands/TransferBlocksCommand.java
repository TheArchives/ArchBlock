package com.archivesmc.archblock.bukkit.commands;

import com.archivesmc.archblock.runnables.RelayRunnable;
import com.archivesmc.archblock.runnables.database.commands.TransferBlocksThread;
import com.archivesmc.archblock.wrappers.bukkit.BukkitPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TransferBlocksCommand implements CommandExecutor {
    private final BukkitPlugin plugin;

    public TransferBlocksCommand(BukkitPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("archblock.transfer")) {
            sender.sendMessage(this.plugin.getPrefixedLocalisedString("command_no_permission"));
        } else {
            if (args.length < 1) {
                sender.sendMessage(this.plugin.getPrefixedLocalisedString("transferblocks_command_usage", label));
            } else {
                UUID uuid = this.plugin.getApi().getUuidForUsername(args[0]);

                if (uuid == null) {
                    sender.sendMessage(this.plugin.getPrefixedLocalisedString("unknown_player", args[0]));
                } else {
                    RelayRunnable callback;

                    if (sender instanceof Player) {
                        callback = new RelayRunnable(this.plugin, sender.getName());
                    } else {
                        sender.sendMessage(this.plugin.getPrefixedLocalisedString("player_only"));
                        return true;
                    }

                    if (this.plugin.isTaskRunning()) {
                        sender.sendMessage(this.plugin.getPrefixedLocalisedString("task_already_running"));
                        return true;
                    }

                    new TransferBlocksThread(this.plugin, ((Player) sender).getUniqueId(), uuid, callback).start();

                    sender.sendMessage(this.plugin.getPrefixedLocalisedString("transferblocks_command_transferring_blocks", args[0]));
                }
            }
        }

        return true;
    }
}
