package com.archivesmc.archblock.runnables.database;

import com.archivesmc.archblock.Plugin;
import com.archivesmc.archblock.storage.entities.Block;
import org.hibernate.Session;

public class SetOwnerThread extends Thread {
    private Plugin plugin;
    private Block block;

    public SetOwnerThread(Plugin plugin, Block block) {
        this.plugin = plugin;
        this.block = block;
    }

    @Override
    public void run() {
        Session s = this.plugin.getSession();

        s.save(this.block);

        s.flush();
        s.close();
    }
}
