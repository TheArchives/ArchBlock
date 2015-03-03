package com.archivesmc.archblock.runnables.database;

import com.archivesmc.archblock.Plugin;
import org.bukkit.block.Block;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;

public class MassOwnershipChanger extends Thread {
    private Plugin plugin;
    private List<Block> blocks;
    private String owner;
    private Runnable finishRunnable;

    public MassOwnershipChanger(Plugin plugin, List<Block> blocks, String owner, Runnable finishRunnable) {
        this.plugin = plugin;
        this.blocks = blocks;
        this.owner = owner;
        this.finishRunnable = finishRunnable;
    }

    @Override
    public void run() {
        Session s = this.plugin.getSession();

        String getOwnerQuery = "SELECT b.uuid FROM Block b WHERE world=:world AND x=:x AND y=:y AND z=:z";
        String updateOwnerQuery = "UPDATE Block b SET b.uuid=:uuid WHERE world=:world AND x=:x AND y=:y AND z=:z";
        String deleteOwnerQuery = "DELETE Block WHERE world=:world AND x=:x AND y=:y AND z=:z";

        Query query;

        String world;
        Object blockOwner;

        Integer x;
        Integer y;
        Integer z;

        for (Block b : this.blocks) {
            world = b.getWorld().getName();
            x = b.getX();
            y = b.getY();
            z = b.getZ();

            try {
                query = s.createQuery(getOwnerQuery);

                query.setString("world", world);
                query.setInteger("x", x);
                query.setInteger("y", y);
                query.setInteger("z", z);

                blockOwner = query.uniqueResult();

                if (blockOwner != null) {
                    if (! blockOwner.equals(this.owner)) {
                        if (this.owner != null) {
                            query = s.createQuery(updateOwnerQuery);

                            query.setString("uuid", this.owner);

                            query.setString("world", world);
                            query.setInteger("x", x);
                            query.setInteger("y", y);
                            query.setInteger("z", z);

                            query.executeUpdate();
                        } else {
                            query = s.createQuery(deleteOwnerQuery);

                            query.setString("world", world);
                            query.setInteger("x", x);
                            query.setInteger("y", y);
                            query.setInteger("z", z);

                            query.executeUpdate();
                        }
                    }
                } else {
                    com.archivesmc.archblock.storage.entities.Block newBlock = new com.archivesmc.archblock.storage.entities.Block(
                            Long.valueOf(x), Long.valueOf(y), Long.valueOf(z), this.owner, world
                    );

                    s.save(newBlock);
                }
            } catch (Exception e) {
                this.plugin.getLogger().warning(String.format(
                        "Unable to update block owner at %s(%s, %s, %s): %s",
                        world, x, y, z, e.getMessage()
                ));
            }
        }

        s.flush();
        s.close();

        this.plugin.getServer().getScheduler().runTask(this.plugin, this.finishRunnable);
    }
}
