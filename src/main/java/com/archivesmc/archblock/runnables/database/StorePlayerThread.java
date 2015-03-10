package com.archivesmc.archblock.runnables.database;

import com.archivesmc.archblock.Plugin;
import com.archivesmc.archblock.storage.entities.Player;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * Thread used to store or update the UUID/username of a player in the database.
 */
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
        Object uuid, username;
        Query q;

        q = s.createQuery("SELECT p.uuid FROM Player p WHERE username=:username");
        q.setString("username", this.player.getUsername());
        uuid = q.uniqueResult();

        q = s.createQuery("SELECT p.username FROM Player p WHERE uuid=:uuid");
        q.setString("uuid", this.player.getUuid());
        username = q.uniqueResult();

        if (this.player.getUuid().equalsIgnoreCase((String) uuid)) {
            // If the UUID was found..
            if (! this.player.getUsername().equalsIgnoreCase((String) username)) {
                // If the username wasn't found..
                q = s.createQuery("UPDATE Player p SET p.username=:username WHERE p.uuid=:uuid");
                q.setString("username", this.player.getUsername());
                q.setString("uuid", (String) uuid);
                q.executeUpdate();
            }
        } else {
            // If the UUID wasn't found..
            if (this.player.getUsername().equalsIgnoreCase((String) username)) {
                // If the username was found..
                q = s.createQuery("UPDATE Player p SET p.uuid=:uuid WHERE p.username=:username");
                q.setString("username", this.player.getUsername());
                q.setString("uuid", (String) uuid);
                q.executeUpdate();
            } else {
                // If the username wasn't found..
                s.save(this.player);
            }
        }

        s.flush();
        s.close();
    }
}
