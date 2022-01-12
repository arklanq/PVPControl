package com.gmail.nowyarek.pvpcontrol.components.logging;

import com.gmail.nowyarek.pvpcontrol.PVPControl;
import com.gmail.nowyarek.pvpcontrol.components.plugin.PluginEnableEvent;
import com.google.common.eventbus.Subscribe;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.EventListener;
import java.util.logging.Level;

@Singleton
public class LoggingConfiguration implements EventListener {
    private final JavaPlugin plugin;
    private final PluginLogger logger;
    private final DefaultLogLevelDetector logLevelDetector;

    @Inject
    public LoggingConfiguration(PVPControl plugin, PluginLogger logger, DefaultLogLevelDetector logLevelDetector) {
        this.plugin = plugin;
        this.logger = logger;
        this.logLevelDetector = logLevelDetector;

        plugin.getEventBus().register(this);
    }

    @Subscribe
    public void onPluginEnable(PluginEnableEvent e) {
        configureDefaultLogLevel();
    }

    public void configureDefaultLogLevel() {
        Level defaultLogLevel = logLevelDetector.detect();
        java.util.logging.Logger julLogger = plugin.getLogger();
        julLogger.setLevel(defaultLogLevel);
        logger.debug("Defined log level.");
    }

}
