package com.archivesmc.archblock.wrappers;

import java.util.List;

public interface Config {
    void reload();
    String getVersion();
    String getLanguage();
    String getDatabaseUsername();
    String getDatabasePassword();
    String getDatabaseDriver();
    String getDatabaseDialect();
    String getDatabaseURL();
    boolean getDatabaseDebug();
    boolean getEnabled();
    boolean getMigrate();
    List<String> getInteractProtected();

    Object getWrapped();
}
