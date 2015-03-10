package com.archivesmc.archblock.runnables.database;

import com.archivesmc.archblock.Plugin;
import com.archivesmc.archblock.storage.entities.Friendship;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * Thread used to create a friendship in the database.
 *
 * Friendships are magic!
 */
public class CreateFriendshipThread extends Thread {
    private final Plugin plugin;
    private final Friendship isMagic;

    public CreateFriendshipThread(Plugin plugin, Friendship isMagic) {
        this.plugin = plugin;
        this.isMagic = isMagic;
    }

    @Override
    public void run() {
        Session s = this.plugin.getSession();
        Query q = s.createQuery("SELECT f FROM Friendship f WHERE playerUuid=:player AND friendUuid=:friend");

        q.setString("player", this.isMagic.getPlayerUuid());
        q.setString("friend", this.isMagic.getFriendUuid());

        Object result = q.uniqueResult();

        if (result == null) {
            s.save(this.isMagic);
        }

        s.flush();
        s.close();
    }
}
