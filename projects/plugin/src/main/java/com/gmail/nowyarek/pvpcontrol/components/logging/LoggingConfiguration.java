package com.gmail.nowyarek.pvpcontrol.components.logging;

import com.gmail.nowyarek.pvpcontrol.PvPControlPlugin;
import org.bukkit.plugin.java.JavaPlugin;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.logging.Level;

@Singleton
public class LoggingConfiguration {
    private final JavaPlugin plugin;
    private final PluginLogger logger;
    private final DefaultLogLevelDetector logLevelDetector;

    @Inject
    public LoggingConfiguration(PvPControlPlugin plugin, PluginLogger logger, DefaultLogLevelDetector logLevelDetector) {
        this.plugin = plugin;
        this.logger = logger;
        this.logLevelDetector = logLevelDetector;

        configureDefaultLogLevel();
    }

    public void configureDefaultLogLevel() {
        Level defaultLogLevel = logLevelDetector.get();
        java.util.logging.Logger julLogger = plugin.getLogger();
        julLogger.setLevel(defaultLogLevel);
        logger.debug(String.format("Defined log level: %s.", defaultLogLevel));
    }

}
