package com.archivesmc.archblock.runnables.database;

import com.archivesmc.archblock.Plugin;
import com.archivesmc.archblock.storage.entities.Player;
import org.hibernate.Session;

public class StorePlayerTh extends Thread {
    private Plugin plugin;
    private Player player;

    public StorePlayerTh(Plugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    @Override
    public void run() {
        Session s = this.plugin.getSession();

        s.saveOrUpdate(this.player);

        s.flush();
        s.close();
    }
}
