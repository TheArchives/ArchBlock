package com.archivesmc.archblock.runnables.database.commands;

import com.archivesmc.archblock.Plugin;
import com.archivesmc.archblock.runnables.RelayRunnable;
import org.bukkit.ChatColor;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.UUID;

public class DisownBlocksPlayerThread extends Thread {
    private final Plugin plugin;
    private final UUID playerUuid;
    private final RelayRunnable callback;

    public DisownBlocksPlayerThread(Plugin plugin, UUID playerUuid, RelayRunnable callback) {
        this.plugin = plugin;
        this.callback = callback;
        this.playerUuid = playerUuid;
    }

    @Override
    public void run() {
        this.plugin.setTaskRunning(true);
        int rows = 0;

        try {
            Session s = this.plugin.getSession();
            Query q = s.createQuery("DELETE Block WHERE uuid=:uuid");

            q.setString("uuid", this.playerUuid.toString());
            rows = q.executeUpdate();

            s.flush();
            s.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        this.plugin.setTaskRunning(false);

        this.callback.setMessage(this.plugin.getPrefixedLocalisedString("disown_thread_complete", rows));

        this.runCallback();
    }

    public void runCallback() {
        this.plugin.getServer().getScheduler().runTask(this.plugin, this.callback);
    }
}
