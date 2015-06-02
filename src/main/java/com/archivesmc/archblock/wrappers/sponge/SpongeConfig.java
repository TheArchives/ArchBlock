package com.archivesmc.archblock.wrappers.sponge;

import com.archivesmc.archblock.wrappers.Config;

import java.util.List;

public class SpongeConfig implements Config {
    @Override
    public void reload() {

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
