package com.archivesmc.archblock.runnables.database;

import com.archivesmc.archblock.Plugin;
import com.archivesmc.archblock.storage.entities.Player;
import org.hibernate.Query;
import org.hibernate.Session;

public class StorePlayerThread extends Thread {
    private final Plugin plugin;
    private final Player player;

    public StorePlayerThread(Plugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    @Override
    public void run() {
        Session s = this.plugin.getSession();

        Query q = s.createQuery("SELECT p.username FROM Player p WHERE uuid=:uuid");

        q.setString("uuid", this.player.getUuid());

        Object result = q.uniqueResult();

        if (result != null) {
            String username = (String) result;

            if (! username.equalsIgnoreCase(this.player.getUsername())) {
                s.saveOrUpdate(this.player);
            }
        } else {
            s.saveOrUpdate(this.player);
        }

        s.flush();
        s.close();
    }
}
