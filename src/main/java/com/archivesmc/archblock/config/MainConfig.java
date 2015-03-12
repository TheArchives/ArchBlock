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
            case "0.0.3":
                this.configuration.set("special_permissions.prevent_burning",                   false);
                this.configuration.set("special_permissions.prevent_damage",                    true);
                this.configuration.set("special_permissions.prevent_fade",                      true);
                this.configuration.set("special_permissions.prevent_form",                      true);
                this.configuration.set("special_permissions.prevent_liquid_flow",               false);
                this.configuration.set("special_permissions.prevent_dragon_egg_teleport",       true);
                this.configuration.set("special_permissions.prevent_growth",                    true);
                this.configuration.set("special_permissions.prevent_ignite",                    true);
                this.configuration.set("special_permissions.prevent_physics",                   false);
                this.configuration.set("special_permissions.prevent_leaf_decay",                true);
                this.configuration.set("special_permissions.prevent_sign_change",               true);
                this.configuration.set("special_permissions.prevent_entity_explode",            false);
                this.configuration.set("special_permissions.prevent_hanging_break",             true);
                this.configuration.set("special_permissions.prevent_hanging_break_by_entity",   true);
                this.configuration.set("special_permissions.prevent_hanging_place",             true);
                this.configuration.set("special_permissions.prevent_lightning_strike",          false);
                this.configuration.set("special_permissions.prevent_player_bucket_empty",       true);
                this.configuration.set("special_permissions.prevent_player_bucket_fill",        true);
                this.configuration.set("special_permissions.prevent_vehicle_create",            true);
                this.configuration.set("version", "0.1.0");
                this.plugin.saveConfig();
                this.plugin.getLogger().info("Updated config to version 0.1.0");
                this.plugin.getLogger().info("You may want to check out all the extra options!");
                this.reload();
                break;
            case "0.1.0": // Latest version
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

    public boolean getSpecialPreventBurning() {
        return this.configuration.getBoolean("special_protections.prevent_burning");
    }

    public boolean getSpecialPreventDamage() {
        return this.configuration.getBoolean("special_protections.prevent_damage");
    }

    public boolean getSpecialPreventFade() {
        return this.configuration.getBoolean("special_protections.prevent_fade");
    }

    public boolean getSpecialPreventForm() {
        return this.configuration.getBoolean("special_protections.prevent_form");
    }

    public boolean getSpecialPreventLiquidFlow() {
        return this.configuration.getBoolean("special_protections.prevent_liquid_flow");
    }

    public boolean getSpecialPreventDragonEggTeleport() {
        return this.configuration.getBoolean("special_protections.prevent_dragon_egg_teleport");
    }

    public boolean getSpecialPreventGrowth() {
        return this.configuration.getBoolean("special_protections.prevent_growth");
    }

    public boolean getSpecialPreventIgnite() {
        return this.configuration.getBoolean("special_protections.prevent_ignite");
    }

    public boolean getSpecialPreventPhysics() {
        return this.configuration.getBoolean("special_protections.prevent_physics");
    }

    public boolean getSpecialPreventLeafDecay() {
        return this.configuration.getBoolean("special_protections.prevent_leaf_decay");
    }

    public boolean getSpecialPreventSignChange() {
        return this.configuration.getBoolean("special_protections.prevent_sign_change");
    }

    public boolean getSpecialPreventEntityExplode() {
        return this.configuration.getBoolean("special_protections.prevent_entity_explode");
    }

    public boolean getSpecialPreventHangingBreak() {
        return this.configuration.getBoolean("special_protections.prevent_hanging_break");
    }

    public boolean getSpecialPreventHangingBreakByEntity() {
        return this.configuration.getBoolean("special_protections.prevent_hanging_break_by_entity");
    }

    public boolean getSpecialPreventHangingPlace() {
        return this.configuration.getBoolean("special_protections.prevent_hanging_place");
    }

    public boolean getSpecialPreventLightningStrike() {
        return this.configuration.getBoolean("special_protections.prevent_lightning_strike");
    }

    public boolean getSpecialPreventBucketEmpty() {
        return this.configuration.getBoolean("special_protections.prevent_player_bucket_empty");
    }

    public boolean getSpecialPreventBucketFill() {
        return this.configuration.getBoolean("special_protections.prevent_player_bucket_fill");
    }

    public boolean getSpecialPreventVehicleCreate() {
        return this.configuration.getBoolean("special_protections.prevent_vehicle_create");
    }
}
