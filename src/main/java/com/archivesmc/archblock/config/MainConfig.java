package com.archivesmc.archblock.config;

import com.archivesmc.archblock.Plugin;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class MainConfig {
    private Plugin plugin;
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

    public String getDatabaseHost() {
        return this.configuration.getString("db_config.host");
    }

    public Integer getDatabasePort() {
        return this.configuration.getInt("db_config.port", 3306);
    }

    public String getDatabaseName() {
        return this.configuration.getString("db_config.database");
    }

    public List<String> getDisabledWorlds() {
        return this.configuration.getStringList("disabled_worlds");
    }
}
