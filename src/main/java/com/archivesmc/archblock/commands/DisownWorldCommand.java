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
        if (!sender.hasPermission("marfprotect.admin")) {
            sender.sendMessage(String.format(
                    "%s[%sSncProtect%s]%s Je hebt geen toegang tot die commando.",
                    ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE, ChatColor.RED
            ));
        } else {
            if (args.length < 1) {
                sender.sendMessage(String.format(
                        "%s[%sSncProtect%s]%s gebruik: %s/%s%s %s<world>",
                        ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE,
                        ChatColor.BLUE, ChatColor.AQUA, ChatColor.DARK_AQUA,
                        label, ChatColor.DARK_GREEN
                ));
            } else {
                if (! this.plugin.getApi().hasWorld(args[0])) {
                    sender.sendMessage(String.format(
                            "%s[%sSncProtect%s]%s Ombekende Wereld: %s%s",
                            ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE,
                            ChatColor.RED, ChatColor.AQUA, args[0]
                    ));
                } else {
                    if (this.plugin.isTaskRunning()) {
                        sender.sendMessage(String.format(
                                "%s[%sSncProtect%s]%s Er is al een taak bezig. Een ogenblik geduld aub.",
                                ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE,
                                ChatColor.RED
                        ));
                        return true;
                    }

                    RelayRunnable callback;

                    if (sender instanceof Player) {
                        callback = new RelayRunnable(this.plugin, sender.getName());
                    } else if (sender instanceof ConsoleCommandSender) {
                        callback = new ConsoleRelayRunnable(this.plugin);
                    } else {
                        sender.sendMessage("This command may only be run by a player or the console.");
                        return true;
                    }

                    new DisownBlocksWorldThread(this.plugin, args[0], callback).start();

                    sender.sendMessage(
                            String.format(
                                    "%s[%sSncProtect%s]%s Verwijdering van block-eigenaars in world: %s%s%s. dit kan even duren.",
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
