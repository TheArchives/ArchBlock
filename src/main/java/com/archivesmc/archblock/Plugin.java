package com.archivesmc.archblock;

import com.archivesmc.archblock.api.ArchBlock;
import com.archivesmc.archblock.commands.*;
import com.archivesmc.archblock.config.MainConfig;
import com.archivesmc.archblock.events.*;
import com.archivesmc.archblock.events.protection.*;
import com.archivesmc.archblock.importers.WatchBlockImporter;
import com.archivesmc.archblock.integrations.WorldGuard;
import com.mewin.WGCustomFlags.WGCustomFlagsPlugin;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.BooleanFlag;
import org.bukkit.plugin.java.JavaPlugin;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * The main plugin class for ArchBlock.
 */
public class Plugin extends JavaPlugin {

    /**
     * BIG TO-DO LIST
     *
     * TODO: HANGING ENTITIES
     * TODO: LIGHTNING STRIKE
     * TODO: BUCKETS
     * TODO: VEHICLES
     *
     * TODO: REMOVE FROM CONFIG: GROW, PHYSICS
     */

    private ArchBlock api;
    private MainConfig mainConfig;
    private SessionFactory sessionFactory;
    private WorldGuard worldGuardIntegration;
    private boolean taskRunning;

    public static final BooleanFlag bypassProtectionFlag = new BooleanFlag("bypass-protection");

    @Override
    public void onLoad() {
        this.api = new ArchBlock(this);
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.mainConfig = new MainConfig(this);

        this.setTaskRunning(false);

        if (!this.mainConfig.getEnabled()) {
            this.getLogger().warning("Plugin is disabled in the config. Set it up or it will do nothing!");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        Configuration hibernateConfiguration = new Configuration().configure()
                .setProperty("hibernate.dialect", this.mainConfig.getDatabaseDialect())                // org.hibernate.dialect.MySQL5Dialect
                .setProperty("hibernate.connection.driver_class", this.mainConfig.getDatabaseDriver()) // com.mysql.jdbc.Driver
                .setProperty("hibernate.connection.url", this.mainConfig.getDatabaseURL())             // jdbc:mysql://localhost:3306/archblock
                .setProperty("hibernate.connection.username", this.mainConfig.getDatabaseUsername())   // correct_username
                .setProperty("hibernate.connection.password", this.mainConfig.getDatabasePassword())   // correct_password
                .setProperty("show_sql", String.valueOf(this.mainConfig.getDatabseDebug()));

        this.sessionFactory = hibernateConfiguration.buildSessionFactory();

        if (this.mainConfig.getMigrate()) {
            if (!this.hasWatchBlockPlugin()) {
                this.getLogger().warning("Migration is enabled, but WatchBlock was not found!");
            } else {
                boolean result = new WatchBlockImporter(this).doImport();

                if (!result) {
                    this.getLogger().warning("Import seems to have failed - please check for errors!");
                }
            }
        }

        Session session = this.getSession();

        this.getLogger().info(String.format(
                "Loaded! Found %s players.", session.createQuery("select count(*) from Player").uniqueResult()
        ));

        session.close();

        // User commands
        this.getCommand("ballow").setExecutor(new FriendCommand(this));
        this.getCommand("blist").setExecutor(new FriendsCommand(this));
        this.getCommand("transferblocks").setExecutor(new TransferBlocksCommand(this));
        this.getCommand("bremove").setExecutor(new UnfriendCommand(this));

        // Staff commands
        this.getCommand("disownplayer").setExecutor(new DisownPlayerCommand(this));
        this.getCommand("disownworld").setExecutor(new DisownWorldCommand(this));
        this.getCommand("setowner").setExecutor(new SetOwnerCommand(this));
        this.getCommand("transferplayer").setExecutor(new TransferPlayerCommand(this));

        this.getServer().getPluginManager().registerEvents(new BlockBreakEvent(this), this);
        this.getServer().getPluginManager().registerEvents(new BlockPlaceEvent(this), this);
        this.getServer().getPluginManager().registerEvents(new PistonMoveEvent(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerBucketEmpty(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerConnectEvent(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerInteractEvent(this), this);

        this.getWGCustomFlagsPlugin().addCustomFlag(Plugin.bypassProtectionFlag);
        this.worldGuardIntegration = new WorldGuard(this);
    }

    @Override
    public void onDisable() {
        if (this.sessionFactory != null) {
            this.sessionFactory.close();
        }
    }

    /**
     * Log an info message when debug mode is enabled in the config
     *
     * @param message The message to log
     */
    public void debug(Object message) {
        if (this.mainConfig.getDatabseDebug()) {
            this.getLogger().info(String.format(
                    "[DEBUG] %s", message
            ));
        }
    }

    /**
     * Get an instance of the API class
     *
     * @return The API
     */
    public ArchBlock getApi() {
        return this.api;
    }

    /**
     * Get a Hibernate database session
     *
     * @return Database session created by the current session factory
     */
    public Session getSession() {
        return this.sessionFactory.openSession();
    }

    /**
     * Get an instance of the WorldEdit plugin, if it's loaded
     *
     * @return WorldEdit plugin, or null if it's not loaded
     */
    public WorldEditPlugin getWorldEdit() {
        return (WorldEditPlugin) this.getServer().getPluginManager().getPlugin("WorldEdit");
    }

    /**
     * Get an instance of the WorldGuard plugin, if it's loaded
     *
     * @return WorldGuard plugin, or null if it's not loaded
     */
    public WorldGuardPlugin getWorldGuard() {
        return (WorldGuardPlugin) this.getServer().getPluginManager().getPlugin("WorldGuard");
    }

    /**
     * Get an instance of the WGCustomFlags plugin, if it's loaded
     *
     * @return WGCustomFlags plugin, or null if it's not loaded
     */
    public WGCustomFlagsPlugin getWGCustomFlagsPlugin() {
        return (WGCustomFlagsPlugin) this.getServer().getPluginManager().getPlugin("WGCustomFlags");
    }

    /**
     * Get the instance of the WorldGuard integration class
     *
     * @return The WorldGuard integration class object
     */
    public WorldGuard getWorldGuardIntegration() {
        return this.worldGuardIntegration;
    }

    /**
     * Check whether the WatchBlock plugin is loaded
     *
     * @return true if WatchBlock is loaded, false otherwise
     */
    public boolean hasWatchBlockPlugin() {
        return this.getServer().getPluginManager().isPluginEnabled("WatchBlock");
    }

    public synchronized boolean isTaskRunning() {
        return this.taskRunning;
    }

    public synchronized void setTaskRunning(boolean taskRunning) {
        this.taskRunning = taskRunning;
    }
}
