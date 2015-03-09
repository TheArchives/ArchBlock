package com.archivesmc.archblock;

import com.archivesmc.archblock.api.ArchBlock;
import com.archivesmc.archblock.commands.FriendCommand;
import com.archivesmc.archblock.commands.FriendsCommand;
import com.archivesmc.archblock.commands.SetOwnerCommand;
import com.archivesmc.archblock.commands.UnfriendCommand;
import com.archivesmc.archblock.config.MainConfig;
import com.archivesmc.archblock.events.BlockBreakEvent;
import com.archivesmc.archblock.events.BlockPlaceEvent;
import com.archivesmc.archblock.events.PistonMoveEvent;
import com.archivesmc.archblock.events.PlayerConnectEvent;
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

public class Plugin extends JavaPlugin {
    private ArchBlock api;
    private MainConfig mainConfig;
    private SessionFactory sessionFactory;
    private WorldGuard worldGuardIntegration;

    public static final BooleanFlag bypassProtectionFlag = new BooleanFlag("bypass-protection");

    @Override
    public void onLoad() {
        this.api = new ArchBlock(this);
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.mainConfig = new MainConfig(this);

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
                .setProperty("show_sql", this.mainConfig.getDatabseDebug().toString());

        this.sessionFactory = hibernateConfiguration.buildSessionFactory();

        if (this.mainConfig.getMigrate()) {
            if (!this.hasWatchBlockPlugin()) {
                this.getLogger().warning("Migration is enabled, but WatchBlock was not found!");
            } else {
                Boolean result = new WatchBlockImporter(this).doImport();

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

        this.getCommand("friend").setExecutor(new FriendCommand(this));
        this.getCommand("friends").setExecutor(new FriendsCommand(this));
        this.getCommand("setowner").setExecutor(new SetOwnerCommand(this));
        this.getCommand("unfriend").setExecutor(new UnfriendCommand(this));

        this.getServer().getPluginManager().registerEvents(new BlockBreakEvent(this), this);
        this.getServer().getPluginManager().registerEvents(new BlockPlaceEvent(this), this);
        this.getServer().getPluginManager().registerEvents(new PistonMoveEvent(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerConnectEvent(this), this);

        this.getWGCustomFlagsPlugin().addCustomFlag(Plugin.bypassProtectionFlag);
        this.worldGuardIntegration = new WorldGuard(this);
    }

    @Override
    public void onDisable() {
        if (this.sessionFactory != null) {
            this.sessionFactory.close();
        }
    }

    public void debug(Object message) {
        if (this.mainConfig.getDatabseDebug()) {
            this.getLogger().info(String.format(
                    "[DEBUG] %s", message
            ));
        }
    }

    public ArchBlock getApi() {
        return this.api;
    }

    public Session getSession() {
        return this.sessionFactory.openSession();
    }

    public WorldEditPlugin getWorldEdit() {
        return (WorldEditPlugin) this.getServer().getPluginManager().getPlugin("WorldEdit");
    }

    public WorldGuardPlugin getWorldGuard() {
        return (WorldGuardPlugin) this.getServer().getPluginManager().getPlugin("WorldGuard");
    }

    public WGCustomFlagsPlugin getWGCustomFlagsPlugin() {
        return (WGCustomFlagsPlugin) this.getServer().getPluginManager().getPlugin("WGCustomFlags");
    }

    public WorldGuard getWorldGuardIntegration() {
        return this.worldGuardIntegration;
    }

    public Boolean hasWatchBlockPlugin() {
        return this.getServer().getPluginManager().isPluginEnabled("WatchBlock");
    }
}
