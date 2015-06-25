package com.archivesmc.archblock.wrappers.sponge;

import com.archivesmc.archblock.utils.Utils;
import com.archivesmc.archblock.wrappers.Config;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

import java.io.*;
import java.util.List;
import java.util.Map;

public class SpongeConfig implements Config {
    // TODO: Finish this
    private final Yaml yaml;
    private final File file;

    private Map data;

    public SpongeConfig(File fh) {
        this.file = fh;
        this.yaml = new Yaml(new SafeConstructor());

        this.reload();
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
        return null;
    }

    @Override
    public String getLanguage() {
        return null;
    }

    @Override
    public String getDatabaseUsername() {
        return null;
    }

    @Override
    public String getDatabasePassword() {
        return null;
    }

    @Override
    public String getDatabaseDriver() {
        return null;
    }

    @Override
    public String getDatabaseDialect() {
        return null;
    }

    @Override
    public String getDatabaseURL() {
        return null;
    }

    @Override
    public boolean getDatabaseDebug() {
        return false;
    }

    @Override
    public boolean getEnabled() {
        return false;
    }

    @Override
    public boolean getMigrate() {
        return false;
    }

    @Override
    public List<String> getInteractProtected() {
        return null;
    }

    @Override
    public Object getWrapped() {
        return null;
    }
}
