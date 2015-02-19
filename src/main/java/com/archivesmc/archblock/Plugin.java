package com.archivesmc.archblock;

import com.archivesmc.archblock.config.MainConfig;
import com.archivesmc.archblock.storage.StorageHandler;
import com.archivesmc.archblock.storage.database.MySQL;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {
    private StorageHandler storageHandler;
    private MainConfig mainConfig;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.mainConfig = new MainConfig(this);

        this.storageHandler = new MySQL(
                this.mainConfig.getDatabaseUsername(),
                this.mainConfig.getDatabasePassword(),
                this.mainConfig.getDatabaseHost(),
                this.mainConfig.getDatabasePort(),
                this.mainConfig.getDatabaseName()
        );

        if (!this.storageHandler.setup()) {
            this.storageHandler.close();
            this.getLogger().warning("Error setting up the database.");
            this.getLogger().warning("This plugin will not work until the problem has been solved.");

            this.getPluginLoader().disablePlugin(this);
            return;
        }
    }

    @Override
    public void onDisable() {
        if (this.storageHandler != null) {
            // Null checks are important! Too many plugins skip these.
            this.storageHandler.close();
        }
    }
}
