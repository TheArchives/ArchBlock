package com.archivesmc.archblock.api;

import com.archivesmc.archblock.Plugin;
import com.archivesmc.archblock.runnables.database.*;
import com.archivesmc.archblock.storage.entities.Friendship;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

/**
 * ArchBlock's main API class. If you need to integrate with the plugin,
 * you may find various way to do so here.
 *
 * You can get an instance of this using Plugin#getApi()
 */
public class ArchBlock {
    // Class is named this in case there are plugins using multiple protection APIs

    private final Plugin plugin;

    public ArchBlock(Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Get an instance of the main ArchBlock plugin
     *
     * @return Current ArchBlock plugin instance
     */
    public Plugin getPlugin() {
        return plugin;
    }

    /**
     * Get the UUID of the player that owns a block
     *
     * @param block The Block to check
     * @return UUID of the block's owner, or null if nobody owns the block
     */
    @Nullable
    public UUID getOwnerUUID(Block block) {
        return this.getOwnerUUID(block.getWorld(), block.getX(), block.getY(), block.getZ());
    }

    /**
     * Get the UUID of the player that owns a block at the given location
     *
     * @param world World object containing the block
     * @param x Block's X coordinate
     * @param y Block's Y coordinate
     * @param z Block's Z coordinate
     * @return UUID of the block's owner, or null if nobody owns the block
     */
    @Nullable
    public UUID getOwnerUUID(World world, int x, int y, int z) {
        return this.getOwnerUUID(world.getName(), x, y, z);
    }

    /**
     * Get the UUID of the player that owns a block
     *
     * @param world Name of the world containing the block
     * @param x Block's X coordinate
     * @param y Block's Y coordinate
     * @param z Block's Z coordinate
     * @return UUID of the block's owner, or null if nobody owns the block
     */
    @Nullable
    public UUID getOwnerUUID(String world, int x, int y, int z) {
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

    /**
     * Set the owner of a block. Note that this method is threaded and returns immediately.
     *
     * @param block The Block to set an owner for
     * @param owner The UUID of the player to set as the owner
     */
    public void setOwnerUUID(Block block, UUID owner) {
        this.setOwnerUUID(block.getWorld(), block.getX(), block.getY(), block.getZ(), owner);
    }

    /**
     * Set the owner of a block. Note that this method is threaded and returns immediately.
     *
     * @param world World object containing the block
     * @param x Block's X coordinate
     * @param y Block's Y coordinate
     * @param z Block's Z coordinate
     * @param owner The UUID of the player to set as the owner
     */
    public void setOwnerUUID(World world, int x, int y, int z, UUID owner) {
        this.setOwnerUUID(world.getName(), x, y, z, owner);
    }

    /**
     * Set the owner of a block. Note that this method is threaded and returns immediately.
     *
     * @param world Name of the world containing the block
     * @param x Block's X coordinate
     * @param y Block's Y coordinate
     * @param z Block's Z coordinate
     * @param owner The UUID of the player to set as the owner
     */
    public void setOwnerUUID(String world, int x, int y, int z, UUID owner) {
        world = world.toLowerCase();

        com.archivesmc.archblock.storage.entities.Block b = new com.archivesmc.archblock.storage.entities.Block(
                (long) x, (long) y, (long) z, owner.toString(), world
        );

        new SetOwnerThread(this.plugin, b).start();
    }

    /**
     * Disown a block. Note that this method is threaded and returns immediately.
     *
     * @param block The Block to disown
     */
    public void removeOwner(Block block){
        this.removeOwner(block.getWorld(), block.getX(), block.getY(), block.getZ());
    }

    /**
     * Disown a block. Note that this method is threaded and returns immediately.
     *
     * @param world World object containing the block
     * @param x Block's X coordinate
     * @param y Block's Y coordinate
     * @param z Block's Z coordinate
     */
    public void removeOwner(World world, int x, int y, int z) {
        this.removeOwner(world.getName(), x, y, z);
    }

    /**
     * Disown a block. Note that this method is threaded and returns immediately.
     *
     * @param world Name of the world containing the block
     * @param x Block's X coordinate
     * @param y Block's Y coordinate
     * @param z Block's Z coordinate
     */
    public void removeOwner(String world, int x, int y, int z) {
        world = world.toLowerCase();

        new RemoveOwnerThread(this.plugin, world, x, y, z).start();
    }

    public boolean hasOwner(Block block) {
        return this.hasOwner(block.getWorld(), block.getX(), block.getY(), block.getZ());
    }

    public boolean hasOwner(World world, int x, int y, int z) {
        return this.hasOwner(world.getName(), x, y, z);
    }

    public boolean hasOwner(String world, int x, int y, int z) {
        Session s = this.plugin.getSession();
        Query q = s.createQuery("SELECT 1 FROM Block b WHERE b=world=:world AND b.x=:x AND b.y=:y AND b.z=:z");

        q.setString("world", world);
        q.setInteger("x", x);
        q.setInteger("y", y);
        q.setInteger("z", z);

        return (q.uniqueResult() != null);
    }

    public boolean hasWorld(String world) {
        Session s = this.plugin.getSession();
        Query q = s.createQuery("SELECT 1 FROM Block b WHERE b.world=:world");

        q.setString("world", world);

        return (q.uniqueResult() != null);
    }

    /**
     * Check whether a player is allowed to edit a block.
     *
     * This takes their permissions, WorldGuard integration, and any applicable
     * friends lists into account.
     *
     * @param block The Block to check against
     * @param player The Player to be checked for
     * @return true if the player is allowed to edit the block, false otherwise
     */
    public boolean canEditBlock(Block block, Player player) {
        if (player.hasPermission("archblock.bypass")) {
            return true;
        }

        if (this.plugin.getWorldGuardIntegration().isInIgnoredRegion(block)) {
            return true;
        }

        UUID owner = this.getOwnerUUID(block);

        if (owner == null) {
            return true;
        }

        if (owner.equals(player.getUniqueId())) {
            return true;
        }

        return this.hasFriendship(owner, player.getUniqueId());
    }

    /**
     * Check whether one player has another in their friends list.
     *
     * @param left The UUID of the player that owns the friends list we wish to check
     * @param right The UUID of the player we want to check the friends list for
     * @return true if the player has the other player in their friends list, false otherwise
     */
    public boolean hasFriendship(UUID left, UUID right) {
        Session s = this.plugin.getSession();
        Query q = s.createQuery("SELECT f FROM Friendship f WHERE playerUuid=? AND friendUuid=?");

        q.setString(0, left.toString());
        q.setString(1, right.toString());

        Object result = q.uniqueResult();

        s.close();

        return result != null;
    }

    /**
     * Create a friendship. Note that this method is threaded and returns immediately.
     *
     * @param left The UUID of the player that owns the friends list we wish to modify
     * @param right The UUID of the player we want to add to the friends list
     */
    public void createFriendship(UUID left, UUID right) {
        Friendship f = new Friendship(left.toString(), right.toString());

        new CreateFriendshipThread(this.plugin, f).start();
    }

    /**
     * Destroy a friendship. Note that this method is threaded and returns immediately.
     *
     * @param left The UUID of the player that owns the friends list we wish to modify
     * @param right The UUID of the player we want to remove from the friends list
     */
    public void destroyFriendship(UUID left, UUID right) {
        new DestroyFriendshipThread(this.plugin, left, right).start();
    }

    /**
     * Get a list of all the UUIDs in a player's friends list.
     *
     * @param left The UUID of the player that owns the friends list we wish to get
     * @return A List of UUIDs of players in the friends list, which will be empty if no results were found
     */
    public List getFriendships(UUID left) {
        Session s = this.plugin.getSession();

        Query q;

        q = s.createQuery(
                "SELECT f.friendUuid FROM Friendship f WHERE f.playerUuid=:uuid"
        );
        q.setString("uuid", left.toString());

        List r = q.list();

        if (r.size() < 1) {
            return r;
        }

        List result = s.createCriteria(com.archivesmc.archblock.storage.entities.Player.class)
                .add(Restrictions.in("uuid", r))
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();

        s.close();

        return result;
    }

    /**
     * Translate a UUID to a username using ArchBlock's database of players.
     *
     * @param uuid The UUID to convert
     * @return The converted username, or null if the UUID isn't in the database
     */
    @Nullable
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

    /**
     * Translate a username to a UUID using ArchBlock's database of players.
     *
     * @param username The username to convert
     * @return The converted UUID, or null if the username isn't in the database
     */
    @Nullable
    public UUID getUuidForUsername(String username) {
        username = username.toLowerCase();

        Session s = this.plugin.getSession();
        Query q = s.createQuery("SELECT p.uuid FROM Player p WHERE username=?");
        q.setString(0, username.toLowerCase());

        Object result = q.uniqueResult();

        s.close();

        if (result == null) {
            return null;
        }

        return UUID.fromString((String) result);
    }

    /**
     * Associate a UUID with a username and store it in the database.
     * Note that this method is threaded and returns immediately.
     *
     * @param uuid The UUID to store
     * @param username The username to store
     */
    public void storePlayer(UUID uuid, String username) {
        username = username.toLowerCase();

        com.archivesmc.archblock.storage.entities.Player p = new com.archivesmc.archblock.storage.entities.Player(
                uuid.toString(), username.toLowerCase()
        );

        this.storePlayer(p);
    }

    /**
     * Associate a UUID with a username and store it in the database.
     * Note that this method is threaded and returns immediately.
     *
     * @param player The Bukkit Player object to store
     */
    public void storePlayer(Player player) {
        this.storePlayer(player.getUniqueId(), player.getName().toLowerCase());
    }

    /**
     * Associate a UUID with a username and store it in the database.
     * Note that this method is threaded and returns immediately.
     *
     * @param player The ArchBlock Player entity to store
     */
    public void storePlayer(com.archivesmc.archblock.storage.entities.Player player) {
        new StorePlayerThread(this.plugin, player).start();
    }
}
