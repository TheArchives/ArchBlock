package com.archivesmc.archblock.bukkit.commands;

import com.archivesmc.archblock.runnables.ConsoleRelayRunnable;
import com.archivesmc.archblock.runnables.RelayRunnable;
import com.archivesmc.archblock.runnables.database.commands.TransferBlocksThread;
import com.archivesmc.archblock.wrappers.bukkit.BukkitPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TransferPlayerCommand implements CommandExecutor {
    private final BukkitPlugin plugin;

    public TransferPlayerCommand(BukkitPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("archblock.admin")) {
            sender.sendMessage(this.plugin.getPrefixedLocalisedString("command_no_permission"));
        } else {
            if (args.length < 2) {
                sender.sendMessage(this.plugin.getPrefixedLocalisedString("transferplayer_command_usage", label));
            } else {
                if (this.plugin.isTaskRunning()) {
                    sender.sendMessage(this.plugin.getPrefixedLocalisedString("task_already_running"));
                    return true;
                }

                UUID fromUuid = this.plugin.getApi().getUuidForUsername(args[0]);
                UUID toUuid = this.plugin.getApi().getUuidForUsername(args[1]);

                if (fromUuid == null) {
                    sender.sendMessage(this.plugin.getPrefixedLocalisedString("unknown_player", args[0]));
                } else if (toUuid == null) {
                    sender.sendMessage(this.plugin.getPrefixedLocalisedString("unknown_player", args[1]));
                } else {
                    RelayRunnable callback;

                    if (sender instanceof Player) {
                        callback = new RelayRunnable(this.plugin, sender.getName());
                    } else if (sender instanceof ConsoleCommandSender) {
                        callback = new ConsoleRelayRunnable(this.plugin);
                    } else {
                        sender.sendMessage(this.plugin.getPrefixedLocalisedString("player_or_console_only"));
                        return true;
                    }

                    new TransferBlocksThread(this.plugin, fromUuid, toUuid, callback).start();

                    sender.sendMessage(
                            this.plugin.getPrefixedLocalisedString(
                                    "transferplayer_command_transferring_blocks", args[0], args[1]
                            )
                    );
                }
            }
        }

        return true;
    }
}
