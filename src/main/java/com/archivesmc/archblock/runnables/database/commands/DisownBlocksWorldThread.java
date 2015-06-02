package com.archivesmc.archblock.runnables.database.commands;

import com.archivesmc.archblock.wrappers.Plugin;
import com.archivesmc.archblock.runnables.RelayRunnable;
import org.hibernate.Query;
import org.hibernate.Session;

public class DisownBlocksWorldThread extends Thread {
    private final Plugin plugin;
    private final String world;
    private final RelayRunnable callback;

    public DisownBlocksWorldThread(Plugin plugin, String world, RelayRunnable callback) {
        this.plugin = plugin;
        this.world = world;
        this.callback = callback;
    }

    @Override
    public void run() {
        this.plugin.setTaskRunning(true);
        int rows = 0;

        try {
            Session s = this.plugin.getSession();
            Query q = s.createQuery("DELETE Block WHERE world=:world");

            q.setString("world", this.world);
            rows = q.executeUpdate();

            s.flush();
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.plugin.setTaskRunning(false);

        this.callback.setMessage(this.plugin.getPrefixedLocalisedString("disownworld_thread_complete", rows));

        this.runCallback();
    }

    public void runCallback() {
        this.plugin.runTask(this.callback);
    }
}
