package com.archivesmc.archblock.runnables;

import com.archivesmc.archblock.Plugin;
import org.bukkit.entity.Player;

public class RelayRunnable implements Runnable {
    private Plugin plugin;
    private String target;
    private String message;

    public RelayRunnable(Plugin plugin, String target, String message) {
        this.plugin = plugin;
        this.target = target;
        this.message = message;
    }

    @Override
    public void run() {
        Player p = this.plugin.getServer().getPlayer(this.target);

        if (p != null) {
            p.sendMessage(this.message);
        }
    }
}
