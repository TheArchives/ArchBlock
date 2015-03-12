package com.archivesmc.archblock.importers;

/**
 * The base Importer interface, so it can do its work without the plugin
 * having to care how it works
 */
public interface Importer {
    /**
     * Do the import
     * @return true if the import succeeded, false otherwise
     */
    public boolean doImport();
}
