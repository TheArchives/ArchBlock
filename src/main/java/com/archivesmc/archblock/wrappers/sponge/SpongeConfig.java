package com.archivesmc.archblock.wrappers.sponge;

import com.archivesmc.archblock.utils.Utils;
import com.archivesmc.archblock.wrappers.Config;
import com.archivesmc.archblock.wrappers.Logger;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpongeConfig implements Config {
    // TODO: Configurate (yaml)
    private final Yaml yaml;
    private final File file;

    private Map data;

    public SpongeConfig(File fh, Logger logger) {
        this.file = fh;
        this.yaml = new Yaml(new SafeConstructor());

        this.reload();

        logger.info(String.format("Data: %s", this.data));
    }

    public void save() throws IOException {
        this.yaml.dump(this.data, new FileWriter(this.file));
    }


    @Override
    public void reload() {
        try {
            if (!this.file.exists()) {
                // Copy default config
                try (InputStream input = getClass().getResourceAsStream("/config.yml");) {
                    String fileData = Utils.convertStreamToString(input);
                    input.close();

                    FileWriter writer = new FileWriter(this.file);
                    writer.write(fileData);
                    writer.flush();
                    writer.close();
                }
            }

            this.data = (Map) this.yaml.load(new FileReader(this.file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getVersion() {
        return (String) this.data.getOrDefault("version", "???");
    }

    @Override
    public String getLanguage() {
        return (String) this.data.getOrDefault("language", "system");
    }

    @Override
    public String getDatabaseUsername() {
        return (String) ((Map<String, Object>) this.data.getOrDefault("db_config", new HashMap<String, Object>())).getOrDefault("username", "archblock");
    }

    @Override
    public String getDatabasePassword() {
        return (String) ((Map<String, Object>) this.data.getOrDefault("db_config", new HashMap<String, Object>())).getOrDefault("password", "");
    }

    @Override
    public String getDatabaseDriver() {
        return (String) ((Map<String, Object>) this.data.getOrDefault("db_config", new HashMap<String, Object>())).getOrDefault("jdbc_driver", "libs.com.mysql.jdbc.Driver");
    }

    @Override
    public String getDatabaseDialect() {
        return (String) ((Map<String, Object>) this.data.getOrDefault("db_config", new HashMap<String, Object>())).getOrDefault("hibernate_dialect", "libs.org.hibernate.dialect.MySQL5InnoDBDialect");
    }

    @Override
    public String getDatabaseURL() {
        return (String) ((Map<String, Object>) this.data.getOrDefault("db_config", new HashMap<String, Object>())).getOrDefault("connection_url", "jdbc:mysql://localhost:3306/archblock");
    }

    @Override
    public boolean getDatabaseDebug() {
        return (Boolean) ((Map<String, Object>) this.data.getOrDefault("db_config", new HashMap<String, Object>())).getOrDefault("debug", false);
    }

    @Override
    public boolean getEnabled() {
        return (Boolean) this.data.getOrDefault("enabled", false);
    }

    @Override
    public boolean getMigrate() {
        return false;  // Not implemented for Sponge
    }

    @Override
    public List<String> getInteractProtected() {
        return (List<String>) this.data.getOrDefault("protect_interact", new ArrayList<String>());
    }

    @Override
    public Object getWrapped() {
        return this.data;
    }
}
