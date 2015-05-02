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

        q = s.createQuery("SELECT p.username FROM Player p WHERE p.uuid=:uuid");
        q.setString("uuid", this.player.getUuid());
        username = q.uniqueResult();

        if (username == null) {
            // Their UUID isn't in the database, let's check for their username.

            this.plugin.debug("UUID isn't in the database.");
        } else if (this.player.getUsername().equalsIgnoreCase((String) username)) {
            // Their username and UUID matches.

            s.flush();
            s.close();
            return;
        } else {
            // Their UUID is there, but the username is wrong. Update it.

            q = s.createQuery("UPDATE Player p SET p.username=:username WHERE p.uuid=:uuid");
            q.setString("username", this.player.getUsername().toLowerCase());
            q.setString("uuid", this.player.getUuid());
            q.executeUpdate();

            s.flush();
            s.close();
            return;
        }

        q = s.createQuery("SELECT p.uuid FROM Player p WHERE p.username=:username");
        q.setString("username", this.player.getUsername().toLowerCase());
        uuid = q.uniqueResult();

        if (uuid == null) {
            // Their username isn't in the database either. Store them.

            s.save(this.player);
            s.flush();
            s.close();
        } else if (this.player.getUuid().equalsIgnoreCase((String) uuid)) {
            // Their username and UUID matches.

            s.flush();
            s.close();
        } else {
            // Their username is there, but the UUID is wrong. Update it.

            q = s.createQuery("UPDATE Player p SET p.uuid=:uuid WHERE p.username=:username");
            q.setString("username", this.player.getUsername().toLowerCase());
            q.setString("uuid", this.player.getUuid());
            q.executeUpdate();

            s.flush();
            s.close();
        }
    }
}
