package com.archivesmc.archblock.wrappers;

import com.archivesmc.archblock.api.ArchBlock;
import com.archivesmc.archblock.integrations.WorldGuard;
import org.hibernate.Session;

public interface Plugin {
    Session getSession();

    WorldGuard getWorldGuardIntegration();
    Logger getWrappedLogger();
    Server getWrappedServer();

    void debug(String message);
    void setTaskRunning(boolean taskRunning);

    String getPrefixedLocalisedString(String key, Object ... args);
    String getLocalisedString(String key, Object ... args);

    void runTask(Runnable runnable);
    void runTaskAsynchronously(Runnable runnable);
    void runTaskLater(Runnable runnable, long delay);
    void runTaskLaterAsynchronously(Runnable runnable, long delay);
    void runTaskTimer(Runnable runnable, long startDelay, long repeatDelay);
    void runTaskTimerAsynchronously(Runnable runnable, long startDelay, long repeatDelay);

    ArchBlock getApi();
}
