package com.archivesmc.archblock.commands;

import com.archivesmc.archblock.Plugin;
import com.archivesmc.archblock.runnables.ConsoleRelayRunnable;
import com.archivesmc.archblock.runnables.RelayRunnable;
import com.archivesmc.archblock.runnables.database.commands.DisownBlocksPlayerThread;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DisownPlayerCommand implements CommandExecutor {
    private final Plugin plugin;

    public DisownPlayerCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("marfprotect.admin")) {
            sender.sendMessage(String.format(
                    "%s[%sSncProtect%s]%s Je hebt geen toegang tot die commando",
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
            } else {
                if (this.plugin.isTaskRunning()) {
                    sender.sendMessage(String.format(
                            "%s[%sSncProtect%s]%s Er is al een taak bezig, een ogenblik geduld aub.",
                            ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE,
                            ChatColor.RED
                    ));
                    return true;
                }

                UUID uuid = this.plugin.getApi().getUuidForUsername(args[0]);

                if (uuid == null) {
                    sender.sendMessage(String.format(
                            "%s[%sSncProtect%s]%s Ombekende speler: %s%s",
                            ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE,
                            ChatColor.RED, ChatColor.AQUA, args[0]
                    ));
                } else {
                    RelayRunnable callback;

                    if (sender instanceof Player) {
                        callback = new RelayRunnable(this.plugin, sender.getName());
                    } else if (sender instanceof ConsoleCommandSender) {
                        callback = new ConsoleRelayRunnable(this.plugin);
                    } else {
                        sender.sendMessage("This command may only be run by a player or the console.");
                        return true;
                    }

                    new DisownBlocksPlayerThread(this.plugin, uuid, callback).start();

                    sender.sendMessage(
                            String.format(
                                    "%s[%sSncProtect%s]%s Verplaast Block eigenaar: %s%s%s. Dit kan even duren.",
                                    ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE,
                                    ChatColor.BLUE, ChatColor.AQUA, args[0], ChatColor.BLUE
                            )
                    );
                }
            }
        }

        return true;
    }
}
