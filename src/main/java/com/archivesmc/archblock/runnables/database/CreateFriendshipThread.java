package com.archivesmc.archblock.runnables.database;

import com.archivesmc.archblock.Plugin;
import com.archivesmc.archblock.storage.entities.Friendship;
import org.hibernate.Session;

public class CreateFriendshipThread extends Thread {
    private Plugin plugin;
    private Friendship isMagic;

    public CreateFriendshipThread(Plugin plugin, Friendship isMagic) {
        this.plugin = plugin;
        this.isMagic = isMagic;
    }

    @Override
    public void run() {
        Session s = this.plugin.getSession();

        s.save(this.isMagic);

        s.flush();
        s.close();
    }
}
