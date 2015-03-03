package com.archivesmc.archblock.runnables.database;

import com.archivesmc.archblock.Plugin;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.UUID;

public class DestroyFriendshipThread extends Thread {
    private Plugin plugin;
    private UUID playerUuid;
    private UUID enemyUuid;

    public DestroyFriendshipThread(Plugin plugin, UUID playerUuid, UUID enemyUuid) {
        this.plugin = plugin;
        this.playerUuid = playerUuid;
        this.enemyUuid = enemyUuid;
    }

    @Override
    public void run() {
        Session s = this.plugin.getSession();
        Query q = s.createQuery("DELETE Friendship WHERE playerUuid=:player AND friendUuid=:enemy");

        q.setString("player", this.playerUuid.toString());
        q.setString("enemy", this.enemyUuid.toString());

        q.executeUpdate();

        s.flush();
        s.close();
    }
}
