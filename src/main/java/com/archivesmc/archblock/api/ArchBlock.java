package com.archivesmc.archblock.api;

import com.archivesmc.archblock.Plugin;
import com.archivesmc.archblock.runnables.database.*;
import com.archivesmc.archblock.storage.entities.Friendship;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;

import java.util.List;
import java.util.UUID;

public class ArchBlock {
    // Class is named this in case there are plugins using multiple protection APIs

    private Plugin plugin;

    public ArchBlock(Plugin plugin) {
        this.plugin = plugin;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public UUID getOwnerUUID(Block block) {
        return this.getOwnerUUID(block.getWorld(), block.getX(), block.getY(), block.getZ());
    }

    public UUID getOwnerUUID(World world, Integer x, Integer y, Integer z) {
        return this.getOwnerUUID(world.getName(), x, y, z);
    }

    public UUID getOwnerUUID(String world, Integer x, Integer y, Integer z) {
        world = world.toLowerCase();

        Session s = this.plugin.getSession();
        Query q = s.createQuery("SELECT b.uuid FROM Block b WHERE world=? AND x=? AND y=? AND z=?");

        q.setString(0, world);
        q.setInteger(1, x);
        q.setInteger(2, y);
        q.setInteger(3, z);

        Object result = q.uniqueResult();

        s.close();

        if (result == null) {
            return null;
        }

        return UUID.fromString((String) result);
    }

    public void setOwnerUUID(Block block, UUID owner) {
        this.setOwnerUUID(block.getWorld(), block.getX(), block.getY(), block.getZ(), owner);
    }

    public void setOwnerUUID(World world, Integer x, Integer y, Integer z, UUID owner) {
        this.setOwnerUUID(world.getName(), x, y, z, owner);
    }

    public void setOwnerUUID(String world, Integer x, Integer y, Integer z, UUID owner) {
        world = world.toLowerCase();

        com.archivesmc.archblock.storage.entities.Block b = new com.archivesmc.archblock.storage.entities.Block(
                Long.valueOf(x), Long.valueOf(y), Long.valueOf(z), owner.toString(), world
        );

        new SetOwnerThread(this.plugin, b).start();
    }

    public void removeOwner(Block block){
        this.removeOwner(block.getWorld(), block.getX(), block.getY(), block.getZ());
    }

    public void removeOwner(World world, Integer x, Integer y, Integer z) {
        this.removeOwner(world.getName(), x, y, z);
    }

    public void removeOwner(String world, Integer x, Integer y, Integer z) {
        world = world.toLowerCase();

        new RemoveOwnerThread(this.plugin, world, x, y, z).start();
    }

    public Boolean hasFriendship(UUID left, UUID right) {
        Session s = this.plugin.getSession();
        Query q = s.createQuery("SELECT f FROM Friendship f WHERE playerUuid=? AND friendUuid=?");

        q.setString(0, left.toString());
        q.setString(1, right.toString());

        Object result = q.uniqueResult();

        s.close();

        return result != null;
    }

    public void createFriendship(UUID left, UUID right) {
        Friendship f = new Friendship(left.toString(), right.toString());

        new CreateFriendshipThread(this.plugin, f).start();
    }

    public void destroyFriendship(UUID left, UUID right) {
        new DestroyFriendshipThread(this.plugin, left, right).start();
    }

    public List getFriendships(UUID left) {
        Session s = this.plugin.getSession();

        Query q;

        q = s.createQuery(
                "SELECT f.friendUuid FROM Friendship f WHERE f.playerUuid=:uuid"
        );
        q.setString("uuid", left.toString());

        List r = q.list();

        List result = s.createCriteria(com.archivesmc.archblock.storage.entities.Player.class)
                .add(Restrictions.in("uuid", r))
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();

        s.close();

        return result;
    }

    public List getFriendships(String username) {
        username = username.toLowerCase();

        Session s = this.plugin.getSession();
        Query q = s.createQuery(
                "SELECT p2.username FROM Friendship f, Player p1, Player p2 WHERE " +
                "p1.username=:username AND f.playerUuid=p1.uuid AND p2.uuid=f.friendUuid"
        );

        q.setString("username", username.toLowerCase());

        return q.list();
    }

    public String getUsernameForUuid(UUID uuid) {
        Session s = this.plugin.getSession();
        Query q = s.createQuery("SELECT p.username FROM Player p WHERE uuid=?");
        q.setString(0, uuid.toString());

        Object result = q.uniqueResult();

        s.close();

        if (result == null) {
            return null;
        }

        return (String) result;
    }

    public String getUuidForUsername(String username) {
        username = username.toLowerCase();

        Session s = this.plugin.getSession();
        Query q = s.createQuery("SELECT p.uuid FROM Player p WHERE username=?");
        q.setString(0, username.toLowerCase());

        Object result = q.uniqueResult();

        s.close();

        if (result == null) {
            return null;
        }

        return (String) result;
    }

    public void storePlayer(UUID uuid, String username) {
        username = username.toLowerCase();

        com.archivesmc.archblock.storage.entities.Player p = new com.archivesmc.archblock.storage.entities.Player(
                uuid.toString(), username.toLowerCase()
        );

        this.storePlayer(p);
    }

    public void storePlayer(Player player) {
        this.storePlayer(player.getUniqueId(), player.getName().toLowerCase());
    }

    public void storePlayer(com.archivesmc.archblock.storage.entities.Player player) {
        new StorePlayerThread(this.plugin, player).start();
    }

    // TODO: Come up with a sane API
}
