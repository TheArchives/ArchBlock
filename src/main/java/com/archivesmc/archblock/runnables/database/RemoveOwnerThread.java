package com.archivesmc.archblock.runnables.database;

import com.archivesmc.archblock.Plugin;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * Thread used to disown a block in the database.
 */
public class RemoveOwnerThread extends Thread {
    private final Plugin plugin;
    private final String world;
    private final int x;
    private final int y;
    private final int z;

    public RemoveOwnerThread(Plugin plugin, String world, int x, int y, int z) {
        this.plugin = plugin;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void run() {
        Session s = this.plugin.getSession();
        Query q;

        q = s.createQuery("SELECT b.uuid FROM Block b WHERE world=:world AND x=:x AND y=:y AND z=:z");

        q.setString("world", this.world);
        q.setInteger("x", this.x);
        q.setInteger("y", this.y);
        q.setInteger("z", this.z);

        Object result = q.uniqueResult();

        if (result != null) {
            q = s.createQuery("DELETE Block WHERE world=:world AND x=:x AND y=:y AND z=:z");

            q.setString("world", this.world);
            q.setInteger("x", this.x);
            q.setInteger("y", this.y);
            q.setInteger("z", this.z);

            q.executeUpdate();
        }

        s.flush();
        s.close();
    }
}
