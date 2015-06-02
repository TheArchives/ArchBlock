package com.archivesmc.archblock.runnables;

import com.archivesmc.archblock.wrappers.Plugin;

public class ConsoleRelayRunnable extends RelayRunnable implements Runnable {
    private final Plugin plugin;
    private String message;

    public ConsoleRelayRunnable(Plugin plugin, String message) {
        super(plugin, "", message);

        this.plugin = plugin;
        this.message = message;
    }

    public ConsoleRelayRunnable(Plugin plugin) {
        super(plugin, "");

        this.plugin = plugin;
        this.message = "";
    }

    @Override
    public void run() {
        this.plugin.getWrappedLogger().info(this.message);
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
