package com.archivesmc.archblock.wrappers.sponge;

import com.archivesmc.archblock.wrappers.Logger;

public class SpongeLogger implements Logger {
    private org.slf4j.Logger wrappedLogger;

    public SpongeLogger(org.slf4j.Logger wrappedLogger) {
        this.wrappedLogger = wrappedLogger;
    }

    @Override
    public void info(String message) {
        this.wrappedLogger.info(message);
    }

    @Override
    public void warning(String message) {
        this.wrappedLogger.warn(message);
    }

    @Override
    public void error(String message) {
        this.wrappedLogger.error(message);
    }

    @Override
    public void critical(String message) {
        this.wrappedLogger.error(message);
    }

    @Override
    public Object getWrapped() {
        return null;
    }
}
