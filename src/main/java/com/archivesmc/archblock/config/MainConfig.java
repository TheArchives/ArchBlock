package com.archivesmc.archblock.config;

import com.archivesmc.archblock.Plugin;
import org.bukkit.configuration.file.FileConfiguration;

public class MainConfig {
    private final Plugin plugin;
    private FileConfiguration configuration;

    public MainConfig(Plugin plugin) {
        this.plugin = plugin;
        this.reload();
    }

    public void reload() {
        this.plugin.reloadConfig();
        this.configuration = this.plugin.getConfig();

        switch(this.getVersion()) {
            case "0.0.1":
                this.configuration.set("version", "0.0.2");
                this.plugin.saveConfig();
                this.reload();
                break;
            case "0.0.2":  // Latest version
                break;
            default:
                this.plugin.getLogger().warning(
                        String.format(
                                "Unknown version \"%s\", please make sure your config.yml is correct.", this.getVersion()
                        )
                );
                break;
        }
    }

    public String getVersion() {
        return this.configuration.getString("version");
    }

    public String getDatabaseUsername() {
        return this.configuration.getString("db_config.username");
    }

    public String getDatabasePassword() {
        return this.configuration.getString("db_config.password");
    }

    public String getDatabaseDriver() {
        return this.configuration.getString("db_config.jdbc_driver");
    }

    public String getDatabaseDialect() {
        return this.configuration.getString("db_config.hibernate_dialect");
    }

    public String getDatabaseURL() {
        return this.configuration.getString("db_config.connection_url");
    }

    public Boolean getDatabseDebug() {
        return this.configuration.getBoolean("db_config.debug");
    }

    public Boolean getEnabled() {
        return this.configuration.getBoolean("enabled", true);
    }

    public Boolean getMigrate() {
        return this.configuration.getBoolean("migrate", true);
    }
}
