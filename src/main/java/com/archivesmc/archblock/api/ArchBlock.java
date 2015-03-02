package com.archivesmc.archblock.api;

import com.archivesmc.archblock.Plugin;
import com.archivesmc.archblock.storage.entities.Friendship;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.hibernate.Query;
import org.hibernate.Session;

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
        Session s = this.plugin.getSession();

        com.archivesmc.archblock.storage.entities.Block b = new com.archivesmc.archblock.storage.entities.Block();

        b.setUuid(owner.toString());
        b.setX(Long.valueOf(x));
        b.setY(Long.valueOf(y));
        b.setZ(Long.valueOf(z));
        b.setWorld(world);

        s.saveOrUpdate(b);
        s.flush();
        s.close();
    }

    public void removeOwner(Block block){
        this.removeOwner(block.getWorld(), block.getX(), block.getY(), block.getZ());
    }

    public void removeOwner(World world, Integer x, Integer y, Integer z) {
        this.removeOwner(world.getName(), x, y, z);
    }

    public void removeOwner(String world, Integer x, Integer y, Integer z) {
        Session s = this.plugin.getSession();
        Query q = s.createQuery("DELETE Block WHERE world=? AND x=? AND y=? AND z=?");

        q.setString(0, world);
        q.setInteger(1, x);
        q.setInteger(2, y);
        q.setInteger(3, z);

        q.executeUpdate();

        s.flush();
        s.close();
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
        Session s = this.plugin.getSession();
        Friendship f = new Friendship();

        f.setPlayerUuid(left.toString());
        f.setFriendUuid(right.toString());

        s.saveOrUpdate(f);

        s.flush();
        s.close();
    }

    public void destroyFriendship(UUID left, UUID right) {
        Session s = this.plugin.getSession();
        Query q = s.createQuery("DELETE Friendship WHERE playerUuid=? AND friendUuid=?");

        q.setString(0, left.toString());
        q.setString(1, right.toString());

        q.executeUpdate();

        s.flush();
        s.close();
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

    public UUID getUuidForUsername(String username) {
        Session s = this.plugin.getSession();
        Query q = s.createQuery("SELECT p.uuid FROM Player p WHERE username=?");
        q.setString(0, username);

        Object result = q.uniqueResult();

        s.close();

        if (result == null) {
            return null;
        }

        return UUID.fromString((String) result);
    }

    public void associateUuidWithUsername(UUID uuid, String username) {
        Session s = this.plugin.getSession();
        com.archivesmc.archblock.storage.entities.Player p = new com.archivesmc.archblock.storage.entities.Player();

        p.setCastedUuid(uuid);
        p.setUsername(username);

        s.saveOrUpdate(p);
        s.flush();
        s.close();
    }

    public void storePlayer(Player player) {
        this.associateUuidWithUsername(player.getUniqueId(), player.getName());
    }

    // TODO: Come up with a sane API
}
