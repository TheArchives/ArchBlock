package com.archivesmc.archblock.wrappers.sponge;

import com.archivesmc.archblock.api.ArchBlock;
import com.archivesmc.archblock.integrations.WorldGuard;
import com.archivesmc.archblock.wrappers.Logger;
import com.archivesmc.archblock.wrappers.Plugin;
import com.archivesmc.archblock.wrappers.Server;
import org.hibernate.Session;

public class SpongePlugin implements Plugin {
    @Override
    public Session getSession() {
        return null;
    }

    @Override
    public WorldGuard getWorldGuardIntegration() {
        return null;
    }

    @Override
    public Logger getWrappedLogger() {
        return null;
    }

    @Override
    public void debug(String message) {

    }

    @Override
    public void setTaskRunning(boolean taskRunning) {

    }

    @Override
    public String getPrefixedLocalisedString(String key, Object... args) {
        return null;
    }

    @Override
    public String getLocalisedString(String key, Object... args) {
        return null;
    }

    @Override
    public void runTask(Runnable runnable) {

    }

    @Override
    public void runTaskAsynchronously(Runnable runnable) {

    }

    @Override
    public void runTaskLater(Runnable runnable, long delay) {

    }

    @Override
    public void runTaskLaterAsynchronously(Runnable runnable, long delay) {

    }

    @Override
    public void runTaskTimer(Runnable runnable, long startDelay, long repeatDelay) {

    }

    @Override
    public void runTaskTimerAsynchronously(Runnable runnable, long startDelay, long repeatDelay) {

    }

    @Override
    public Server getWrappedServer() {
        return null;
    }

    @Override
    public ArchBlock getApi() {
        return null;
    }
}
