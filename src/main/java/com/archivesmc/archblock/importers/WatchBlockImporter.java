package com.archivesmc.archblock.importers;

import com.archivesmc.archblock.Plugin;

public class WatchBlockImporter implements Importer{
    private Plugin plugin;

    public WatchBlockImporter(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Boolean doImport() {
        return null;
    }
}
