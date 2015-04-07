package com.archivesmc.archblock.runnables.database.commands;

import com.archivesmc.archblock.Plugin;
import com.archivesmc.archblock.runnables.RelayRunnable;
import org.bukkit.ChatColor;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.UUID;

public class TransferBlocksThread extends Thread {
    private final Plugin plugin;
    private final UUID fromPlayer;
    private final UUID toPlayer;
    private final RelayRunnable callback;

    public TransferBlocksThread(Plugin plugin, UUID fromPlayer, UUID toPlayer, RelayRunnable callback) {
        this.plugin = plugin;
        this.fromPlayer = fromPlayer;
        this.toPlayer = toPlayer;
        this.callback = callback;
    }

    @Override
    public void run() {
        this.plugin.setTaskRunning(true);
        int rows = 0;

        try {
            Session s = this.plugin.getSession();
            Query q = s.createQuery("UPDATE Block b SET b.uuid=:toUuid WHERE b.uuid=:fromUuid");

            q.setString("toUuid", this.toPlayer.toString());
            q.setString("fromUuid", this.fromPlayer.toString());
            rows = q.executeUpdate();

            s.flush();
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.plugin.setTaskRunning(false);

        this.callback.setMessage(this.plugin.getPrefixedLocalisedString("transferblocks_thread_complete", rows));

        this.runCallback();
    }

    public void runCallback() {
        this.plugin.getServer().getScheduler().runTask(this.plugin, this.callback);
    }
}
