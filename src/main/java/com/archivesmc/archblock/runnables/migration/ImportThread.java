package com.archivesmc.archblock.runnables.migration;

import com.archivesmc.archblock.Plugin;
import com.archivesmc.archblock.storage.entities.Block;
import com.archivesmc.archblock.utils.Point3D;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;
import java.util.Map;

/**
 * Thread used to import a set of blocks that have been converted from some other format.
 */
public class ImportThread extends Thread {
    private final String world;
    private final List<Map<Point3D, String>> points;
    private final Plugin plugin;

    private boolean done;

    public ImportThread(String world, List<Map<Point3D, String>> points, Plugin plugin) {
        this.world = world;
        this.points = points;
        this.plugin = plugin;

        this.done = false;
    }

    @Override
    public void run() {
        Session s = this.plugin.getSession();
        Query ownerQuery = s.createQuery("SELECT b.uuid FROM Block b WHERE world=? AND x=? AND y=? AND z=?");
        Query deleteQuery = s.createQuery("DELETE Block WHERE world=? AND x=? AND y=? AND z=?");

        Point3D blockPoint;
        String uuid;
        Object result;
        Integer doneBlocks = 0;

        for (Map<Point3D, String> chunk : this.points) {
            for (Map.Entry<Point3D, String> block : chunk.entrySet()) {
                blockPoint = block.getKey();
                uuid = block.getValue();

                ownerQuery.setString(0, world);
                ownerQuery.setInteger(1, blockPoint.getX());
                ownerQuery.setInteger(2, blockPoint.getY());
                ownerQuery.setInteger(3, blockPoint.getZ());

                result = ownerQuery.uniqueResult();

                if (result != null) {
                    if (result.equals(uuid)) {
                        continue;
                    }

                    deleteQuery.setString(0, world);
                    deleteQuery.setInteger(1, blockPoint.getX());
                    deleteQuery.setInteger(2, blockPoint.getY());
                    deleteQuery.setInteger(3, blockPoint.getZ());

                    deleteQuery.executeUpdate();
                }

                s.save(new Block(
                        Long.valueOf(blockPoint.getX()),
                        Long.valueOf(blockPoint.getY()),
                        Long.valueOf(blockPoint.getZ()),
                        uuid,
                        world
                ));

                doneBlocks += 1;

                if (doneBlocks % 250 == 0) {
                    s.flush();
                    s.clear();
                }
            }

            s.flush();
            s.clear();

            doneBlocks = 0;
        }

        s.flush();
        s.close();

        this.setDone();
    }

    public synchronized Boolean getDone() {
        return done;
    }
    
    public synchronized void setDone() {
        this.done = true;
    }

    public int getNumberOfChunks() {
        return this.points.size();
    }
}
