package com.archivesmc.archblock.runnables.database.commands;

import com.archivesmc.archblock.wrappers.Block;
import com.archivesmc.archblock.wrappers.Plugin;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;
import java.util.UUID;

/**
 * Thread used to set or remove the owner for a large number of blocks at once.
 *
 * This is used by the /setowner command.
 */
public class MassOwnershipChanger extends Thread {
    private final Plugin plugin;
    private final List<Block> blocks;
    private final UUID owner;
    private final Runnable finishRunnable;

    public MassOwnershipChanger(Plugin plugin, List<Block> blocks, UUID owner, Runnable finishRunnable) {
        this.plugin = plugin;
        this.blocks = blocks;
        this.owner = owner;
        this.finishRunnable = finishRunnable;
    }

    @Override
    public void run() {
        this.plugin.setTaskRunning(true);

        try {
            Session s = this.plugin.getSession();

            String getOwnerQuery = "SELECT b.uuid FROM Block b WHERE world=:world AND x=:x AND y=:y AND z=:z";
            String updateOwnerQuery = "UPDATE Block b SET b.uuid=:uuid WHERE world=:world AND x=:x AND y=:y AND z=:z";
            String deleteOwnerQuery = "DELETE Block WHERE world=:world AND x=:x AND y=:y AND z=:z";

            Query query;

            String world;
            Object blockOwner;

            int x;
            int y;
            int z;

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
                        if (!UUID.fromString((String) blockOwner).equals(this.owner)) {
                            if (this.owner != null) {
                                query = s.createQuery(updateOwnerQuery);

                                query.setString("uuid", this.owner.toString());

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
                                (long) x, (long) y, (long) z, this.owner, world
                        );

                        s.save(newBlock);
                    }
                } catch (Exception e) {
                    this.plugin.getWrappedLogger().warning(this.plugin.getLocalisedString(
                            "unable_to_update_block_owner",
                            world, x, y, z, e.getMessage()
                    ));
                }
            }

            s.flush();
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.plugin.setTaskRunning(false);
        this.plugin.runTask(this.finishRunnable);
    }
}
