package com.archivesmc.archblock.runnables.database;

import com.archivesmc.archblock.Plugin;
import com.archivesmc.archblock.storage.entities.Block;
import org.hibernate.Query;
import org.hibernate.Session;

public class SetOwnerThread extends Thread {
    private final Plugin plugin;
    private final Block block;

    public SetOwnerThread(Plugin plugin, Block block) {
        this.plugin = plugin;
        this.block = block;
    }

    @Override
    public void run() {
        Session s = this.plugin.getSession();
        Query q;

        q = s.createQuery("SELECT b.uuid FROM Block b WHERE world=:world AND x=:x AND y=:y AND z=:z");

        q.setString("world", this.block.getWorld());
        q.setLong("x", this.block.getX());
        q.setLong("y", this.block.getY());
        q.setLong("z", this.block.getZ());

        Object result = q.uniqueResult();

        if (result != null) {
            q = s.createQuery("DELETE Block WHERE world=:world AND x=:x AND y=:y AND z=:z");

            q.setString("world", this.block.getWorld());
            q.setLong("x", this.block.getX());
            q.setLong("y", this.block.getY());
            q.setLong("z", this.block.getZ());

            q.executeUpdate();
        }

        s.save(this.block);

        s.flush();
        s.close();
    }
}
