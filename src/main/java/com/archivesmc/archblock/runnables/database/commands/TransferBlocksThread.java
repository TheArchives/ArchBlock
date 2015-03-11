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

        Session s = this.plugin.getSession();
        Query q = s.createQuery("UPDATE Block b SET b.uuid=:toUuid WHERE p.uuid=:fromUuid");

        q.setString("toUuid", this.toPlayer.toString());
        q.setString("fromUuid", this.fromPlayer.toString());
        int rows = q.executeUpdate();

        s.flush();
        s.close();

        this.plugin.setTaskRunning(false);

        this.callback.setMessage(
                String.format(
                        "%s[%sArchBlock%s]%s Ownership of %s%s blocks%s has been changed.",
                        ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE, ChatColor.BLUE,
                        ChatColor.RED, rows, ChatColor.BLUE
                )
        );

        this.runCallback();
    }

    public void runCallback() {
        this.plugin.getServer().getScheduler().runTask(this.plugin, this.callback);
    }
}
