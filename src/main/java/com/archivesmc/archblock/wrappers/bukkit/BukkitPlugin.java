package com.archivesmc.archblock.wrappers.bukkit;

import com.archivesmc.archblock.api.ArchBlock;
import com.archivesmc.archblock.bukkit.commands.*;
import com.archivesmc.archblock.bukkit.events.PlayerConnectEvent;
import com.archivesmc.archblock.bukkit.events.protection.*;
import com.archivesmc.archblock.bukkit.importers.WatchBlockImporter;
import com.archivesmc.archblock.integrations.WorldGuard;
import com.archivesmc.archblock.wrappers.Config;
import com.archivesmc.archblock.wrappers.Logger;
import com.archivesmc.archblock.wrappers.Plugin;
import com.archivesmc.archblock.wrappers.Server;
import com.mewin.WGCustomFlags.WGCustomFlagsPlugin;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.BooleanFlag;
import org.bukkit.ChatColor;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class BukkitPlugin extends JavaPlugin implements Plugin {
    private ArchBlock api;
    private Config mainConfig;
    private SessionFactory sessionFactory;
    private WorldGuard worldGuardIntegration;
    private boolean taskRunning;

    private BukkitLogger wrappedLogger;
    private BukkitServer wrappedServer;

    private ResourceBundle localisedStrings;

    public static final BooleanFlag bypassProtectionFlag = new BooleanFlag("bypass-protection");

    @Override
    public void onLoad() {
        this.api = new ArchBlock(this);
        this.wrappedLogger = new BukkitLogger(this.getLogger());
        this.wrappedServer = new BukkitServer(this.getServer());
}

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.mainConfig = new BukkitConfig(this);
        this.setUpLocalisation();
        this.mainConfig.reload();

        this.setTaskRunning(false);

        if (!this.mainConfig.getEnabled()) {
            this.getLogger().warning(this.getLocalisedString("plugin_disabled_config"));
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Test the database connection
        this.getLogger().info("Testing database connection..");

        try {
            Connection conn;
            Properties connectionProps = new Properties();

            connectionProps.put("user", this.mainConfig.getDatabaseUsername());
            connectionProps.put("password", this.mainConfig.getDatabasePassword());

            Class.forName(this.mainConfig.getDatabaseDriver());

            conn = DriverManager.getConnection(this.mainConfig.getDatabaseURL(), connectionProps);
            conn.getMetaData();
            conn.close();
        } catch (ClassNotFoundException e) {
            this.getLogger().severe("Unable to find database driver! Please check your settings.");
            e.printStackTrace();
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        } catch (SQLException e) {
            this.getLogger().severe("Unable to connect to database! Please check your settings.");
            e.printStackTrace();
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.getLogger().info("Looks good, continuing..");

        Configuration hibernateConfiguration = new Configuration().configure()
                .setProperty("hibernate.dialect", this.mainConfig.getDatabaseDialect())                // org.hibernate.dialect.MySQL5Dialect
                .setProperty("hibernate.connection.driver_class", this.mainConfig.getDatabaseDriver()) // com.mysql.jdbc.Driver
                .setProperty("hibernate.connection.url", this.mainConfig.getDatabaseURL())             // jdbc:mysql://localhost:3306/archblock
                .setProperty("hibernate.connection.username", this.mainConfig.getDatabaseUsername())   // correct_username
                .setProperty("hibernate.connection.password", this.mainConfig.getDatabasePassword())   // correct_password
                .setProperty("show_sql", String.valueOf(this.mainConfig.getDatabaseDebug()));

        this.sessionFactory = hibernateConfiguration.buildSessionFactory();

        if (this.mainConfig.getMigrate()) {
            if (!this.hasWatchBlockPlugin()) {
                this.getLogger().warning(this.getLocalisedString("plugin_migration_no_watchblock"));
            } else {
                boolean result = new WatchBlockImporter(this).doImport();

                if (!result) {
                    this.getLogger().warning(this.getLocalisedString("plugin_migration_failed"));
                }
            }
        }

        Session session = this.getSession();

        this.getLogger().info(this.getLocalisedString(
                "plugin_loaded_players", session.createQuery("select count(*) from Player").uniqueResult()
        ));

        session.close();

        // User commands
        this.getCommand("friend").setExecutor(new FriendCommand(this));
        this.addCommandAliases("friend");
        this.getCommand("friends").setExecutor(new FriendsCommand(this));
        this.addCommandAliases("friends");
        this.getCommand("transferblocks").setExecutor(new TransferBlocksCommand(this));
        this.addCommandAliases("transferblocks");
        this.getCommand("unfriend").setExecutor(new UnfriendCommand(this));
        this.addCommandAliases("unfriend");

        // Staff commands
        this.getCommand("disownplayer").setExecutor(new DisownPlayerCommand(this));
        this.addCommandAliases("disownplayer");
        this.getCommand("disownworld").setExecutor(new DisownWorldCommand(this));
        this.addCommandAliases("disownworld");
        this.getCommand("setowner").setExecutor(new SetOwnerCommand(this));
        this.addCommandAliases("setowner");
        this.getCommand("transferplayer").setExecutor(new TransferPlayerCommand(this));
        this.addCommandAliases("transferplayer");

        this.getServer().getPluginManager().registerEvents(new BlockBreakEvent(this), this);
        this.getServer().getPluginManager().registerEvents(new BlockPlaceEvent(this), this);
        this.getServer().getPluginManager().registerEvents(new PistonMoveEvent(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerBucketEmpty(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerConnectEvent(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerInteractEvent(this), this);

        this.getWGCustomFlagsPlugin().addCustomFlag(bypassProtectionFlag);
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
    @Override
    public void debug(String message) {
        if (this.mainConfig.getDatabaseDebug()) {
            this.getWrappedLogger().info(String.format(
                    "[DEBUG] %s", message
            ));
        }
    }

    /**
     * Get an instance of the API class
     *
     * @return The API
     */
    @Override
    public ArchBlock getApi() {
        return this.api;
    }

    /**
     * Get a Hibernate database session
     *
     * @return Database session created by the current session factory
     */

    @Override
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

    @Override
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

    public List<String> getInteractProtected() {
        return this.mainConfig.getInteractProtected();
    }

    public void setUpLocalisation() {
        String lang = this.mainConfig.getLanguage();
        Locale l;

        if (lang.equalsIgnoreCase("system")) {
            l = Locale.getDefault();
        } else {
            l = Locale.forLanguageTag(lang);
        }

        this.localisedStrings = ResourceBundle.getBundle("translations.Messages", l);
    }

    @Override
    public String getPrefixedLocalisedString(String key, Object ... args) {
        return String.format(
                "%s %s",
                this.getLocalisedString("message_prefix"),
                this.getLocalisedString(key, args)
        );
    }

    @Override
    public String getLocalisedString(String key, Object ... args) {
        String value;

        try {
            value = this.localisedStrings.getString(key);
        } catch (MissingResourceException e) {
            return null;
        }

        return ChatColor.translateAlternateColorCodes('&', String.format(value, args));
    }

    public void addCommandAliases(String command) {
        // We have to use reflection to register aliases programmatically
        Field commandMap;
        SimpleCommandMap simplecommandMap;

        try {
            // Start with the plugin manager
            PluginManager manager = this.getServer().getPluginManager();

            // Check that it's an instance we can use
            if (! (manager instanceof SimplePluginManager)) {
                // If not, return, we can't register aliases
                return;
            }

            // If so, store that instance
            SimplePluginManager simplePluginManager = (SimplePluginManager) manager;

            // Get the command map that needs to be modified
            commandMap = simplePluginManager.getClass().getDeclaredField("commandMap");
            commandMap.setAccessible(true);

            // And get the instance
            simplecommandMap = (SimpleCommandMap) commandMap.get(simplePluginManager);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return;
        }

        // Now, get our localised aliases
        String commandKey = String.format("alias_%s", command);
        String localisedAliases = this.getLocalisedString(commandKey);

        if (localisedAliases == null) {
            // No aliases
            return;
        }

        // Get a pointer to the command's aliases
        List<String> aliases = this.getCommand(command).getAliases();

        for (String s : localisedAliases.split(";")) {
            // Loop over and add aliases
            if (! aliases.contains(s)) {
                aliases.add(s);

                // Now, register them using reflection!
                simplecommandMap.register(s, this.getDescription().getName(), this.getCommand(command));
            }
        }
    }

    @Override
    public void runTask(Runnable runnable) {
        this.getServer().getScheduler().runTask(this, runnable);
    }

    @Override
    public void runTaskAsynchronously(Runnable runnable) {
        this.getServer().getScheduler().runTaskAsynchronously(this, runnable);
    }

    @Override
    public void runTaskLater(Runnable runnable, long delay) {
        this.getServer().getScheduler().runTaskLater(this, runnable, delay);
    }

    @Override
    public void runTaskLaterAsynchronously(Runnable runnable, long delay) {
        this.getServer().getScheduler().runTaskLaterAsynchronously(this, runnable, delay);
    }

    @Override
    public void runTaskTimer(Runnable runnable, long startDelay, long repeatDelay) {
        this.getServer().getScheduler().runTaskTimer(this, runnable, startDelay, repeatDelay);
    }

    @Override
    public void runTaskTimerAsynchronously(Runnable runnable, long startDelay, long repeatDelay) {
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, runnable, startDelay, repeatDelay);
    }

    @Override
    public Logger getWrappedLogger() {
        return this.wrappedLogger;
    }

    @Override
    public Server getWrappedServer() {
        return this.wrappedServer;
    }
}
