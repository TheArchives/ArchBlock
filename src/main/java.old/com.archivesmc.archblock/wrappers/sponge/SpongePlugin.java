package com.archivesmc.archblock.wrappers.sponge;

import com.archivesmc.archblock.api.ArchBlock;
import com.archivesmc.archblock.integrations.WorldGuard;
import com.archivesmc.archblock.wrappers.Config;
import com.archivesmc.archblock.wrappers.Logger;
import com.archivesmc.archblock.wrappers.Plugin;
import com.archivesmc.archblock.wrappers.Server;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.hibernate.Session;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.state.InitializationEvent;
import org.spongepowered.api.event.state.PreInitializationEvent;
import org.spongepowered.api.text.Texts;

import java.io.File;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

@org.spongepowered.api.plugin.Plugin(id = "archblock", name = "ArchBlock", version = "0.1.1")
public class SpongePlugin implements Plugin {
    // TODO: Finish this
    private Logger logger;

    private ResourceBundle localisedStrings;
    private Config mainConfig;

    @Inject @Named("PluginConfigDir")
    private File configDir;

    @Inject
    public SpongePlugin(org.slf4j.Logger logger) {
        this.logger = new SpongeLogger(logger);
    }

    @Subscribe
    public void onPreInitialization (PreInitializationEvent event) {
        if (!this.configDir.exists()) {
            if (!this.configDir.mkdir()) {
                this.logger.error("Unable to create configuration folder");
            }
        }

        this.mainConfig = new SpongeConfig(new File(this.configDir, "config.yml"), this.logger);
    }

    @Subscribe
    public void onInitialization(InitializationEvent event) {

    }

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
        return this.logger;
    }

    @Override
    public void debug(String message) {

    }

    @Override
    public void setTaskRunning(boolean taskRunning) {

    }

    @Override
    public String getPrefixedLocalisedString(String key, Object ... args) {
        return String.format(
                "%s %s",
                this.getLocalisedString("message_prefix"),
                this.getLocalisedString(key, args)
        );
    }

    @Override
    public String getLocalisedString(String key, Object ... args) {
        String value;

        try {
            value = this.localisedStrings.getString(key);
        } catch (MissingResourceException e) {
            return null;
        }

        return Texts.replaceCodes(String.format(value, args), '&');
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
