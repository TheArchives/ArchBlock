package com.archivesmc.archblock.config;

import com.archivesmc.archblock.Plugin;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * The main configuration class, which handles abstraction of the plugin's configuration,
 * and automatically upgrades old configuration files when the plugin is updated.
 */
public class MainConfig {
    private final Plugin plugin;
    private FileConfiguration configuration;

    public MainConfig(Plugin plugin) {
        this.plugin = plugin;
        this.reload();
    }

    /**
     * Reload the configuration, and perform any upgrades that need to be done.
     */
    public void reload() {
        this.plugin.reloadConfig();
        this.configuration = this.plugin.getConfig();

        switch(this.getVersion()) {
            case "0.0.1":
                this.configuration.set("version", "0.0.2");
                this.plugin.saveConfig();
                this.plugin.getLogger().info("Updated config to version 0.0.2");
                this.reload();
                break;
            case "0.0.2":
                this.configuration.set("version", "0.0.3");
                this.plugin.saveConfig();
                this.plugin.getLogger().info("Updated config to version 0.0.3");
                this.reload();
                break;
            case "0.0.3":  // Latest version
                break;
            default:
                this.plugin.getLogger().warning(
                        String.format(
                                "Unknown version \"%s\", please make sure your config.yml is correct.",
                                this.getVersion()
                        )
                );
                break;
        }
    }

    /**
     * @return The version of the configuration file
     */
    public String getVersion() {
        return this.configuration.getString("version");
    }

    /**
     * @return The username used to connect to the database
     */
    public String getDatabaseUsername() {
        return this.configuration.getString("db_config.username");
    }

    /**
     * @return The password used to connect to the database
     */
    public String getDatabasePassword() {
        return this.configuration.getString("db_config.password");
    }

    /**
     * @return The configured JDBC driver class
     */
    public String getDatabaseDriver() {
        return this.configuration.getString("db_config.jdbc_driver");
    }

    /**
     * @return The configured Hibernate dialect to use with the database
     */
    public String getDatabaseDialect() {
        return this.configuration.getString("db_config.hibernate_dialect");
    }

    /**
     * @return The JDBC connection URL for the database
     */
    public String getDatabaseURL() {
        return this.configuration.getString("db_config.connection_url");
    }

    /**
     * @return Whether debug mode has been enabled in the configuration
     */
    public boolean getDatabseDebug() {
        return this.configuration.getBoolean("db_config.debug");
    }

    /**
     * @return Whether the plugin has been enabled in the configuration
     */
    public boolean getEnabled() {
        return this.configuration.getBoolean("enabled", true);
    }

    /**
     * @return Whether data migration has been enabled in the configuration
     */
    public boolean getMigrate() {
        return this.configuration.getBoolean("migrate", true);
    }
}
