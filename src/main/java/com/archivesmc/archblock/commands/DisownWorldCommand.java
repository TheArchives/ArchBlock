package com.archivesmc.archblock.commands;

import com.archivesmc.archblock.Plugin;
import com.archivesmc.archblock.runnables.ConsoleRelayRunnable;
import com.archivesmc.archblock.runnables.RelayRunnable;
import com.archivesmc.archblock.runnables.database.commands.DisownBlocksWorldThread;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class DisownWorldCommand implements CommandExecutor {
    private final Plugin plugin;

    public DisownWorldCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("archblock.admin")) {
            sender.sendMessage(this.plugin.getPrefixedLocalisedString("command_no_permission"));
        } else {
            if (args.length < 1) {
                sender.sendMessage(this.plugin.getPrefixedLocalisedString("disownworld_command_usage", label));
            } else {
                if (! this.plugin.getApi().hasWorld(args[0])) {
                    sender.sendMessage(this.plugin.getPrefixedLocalisedString("unknown_world", args[0]));
                } else {
                    if (this.plugin.isTaskRunning()) {
                        sender.sendMessage(this.plugin.getPrefixedLocalisedString("task_already_running"));
                        return true;
                    }

                    RelayRunnable callback;

                    if (sender instanceof Player) {
                        callback = new RelayRunnable(this.plugin, sender.getName());
                    } else if (sender instanceof ConsoleCommandSender) {
                        callback = new ConsoleRelayRunnable(this.plugin);
                    } else {
                        sender.sendMessage(this.plugin.getLocalisedString("player_or_console_only"));
                        return true;
                    }

                    new DisownBlocksWorldThread(this.plugin, args[0], callback).start();

                    sender.sendMessage(
                            this.plugin.getPrefixedLocalisedString("disownworld_command_disowning_blocks", args[0])
                    );
                }
            }
        }

        return true;
    }
}
