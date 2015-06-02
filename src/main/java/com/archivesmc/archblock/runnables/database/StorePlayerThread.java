package com.archivesmc.archblock.runnables.database;

import com.archivesmc.archblock.wrappers.Plugin;
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

        q = s.createQuery("SELECT p.uuid FROM Player p WHERE p.username=:username");
        q.setString("username", this.player.getUsername());
        uuid = q.uniqueResult();

        if (
                username != null && uuid != null // Username and UUID in the DB aren't null
             && (! this.player.getUsername().equalsIgnoreCase((String) username))  // Username found isn't this player's
             && (! this.player.getUuid().equalsIgnoreCase((String) uuid))) {  // UUID isn't this player's either
            // The UUID and username don't match
            // Two players have swapped their usernames. This is a really sucky thing to do, but we have to deal with it

            /**
             * Variable pairs:
             * this.player.getUsername() -> uuid
             * username -> this.player.getUuid()
             *
             * What we want:
             *
             * this.player.getUsername() -> this.player.getUuid()
             * username -> uuid
             */

            this.plugin.getWrappedLogger().warning(String.format(
                    "It looks like the players %s and %s have swapped their usernames.",
                    this.player.getUsername(), username
            ));
            this.plugin.getWrappedLogger().warning("The database is being updated - let's hope they aren't up to anything shady!");

            // Firstly, delete both players from the database.
            q = s.createQuery("DELETE Player WHERE uuid=:uuid");
            q.setString("uuid", (String) uuid);
            q.executeUpdate();

            q = s.createQuery("DELETE Player WHERE uuid=:uuid");
            q.setString("uuid", this.player.getUuid());
            q.executeUpdate();

            // Now, re-insert both players.
            s.save(this.player);
            s.save(new Player((String) uuid, (String) username));

            // Hopefully, this should have solved all of our problems.
            s.flush();
            s.close();
            return;
        }

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
