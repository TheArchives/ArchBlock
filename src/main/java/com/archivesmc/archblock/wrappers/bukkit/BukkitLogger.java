package com.archivesmc.archblock.wrappers.bukkit;

import com.archivesmc.archblock.wrappers.Logger;

public class BukkitLogger implements Logger {
    java.util.logging.Logger wrappedLogger;

    public BukkitLogger(java.util.logging.Logger logger) {
        this.wrappedLogger = logger;
    }

    @Override
    public void info(String message) {
        this.wrappedLogger.info(message);
    }

    @Override
    public void warning(String message) {
        this.wrappedLogger.warning(message);
    }

    @Override
    public void error(String message) {
        this.wrappedLogger.severe(message);
    }

    @Override
    public void critical(String message) {
        this.wrappedLogger.severe(message);
    }

    @Override
    public Object getWrapped() {
        return this.wrappedLogger;
    }
}
