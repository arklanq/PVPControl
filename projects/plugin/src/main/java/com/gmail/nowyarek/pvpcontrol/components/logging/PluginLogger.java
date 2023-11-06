package com.gmail.nowyarek.pvpcontrol.components.logging;

import org.bukkit.plugin.java.JavaPlugin;

import jakarta.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PluginLogger {
    private final Logger logger;

    @Inject
    public PluginLogger(JavaPlugin plugin) {
        this.logger = plugin.getLogger();
    }

    public void error(String message) {
        logger.severe(message);
    }

    public void error(Throwable e) {
        e.printStackTrace();
    }

    public void error(String message, Throwable e) {
        logger.severe(message);
        e.printStackTrace();
    }

    public void warn(String message) {
        logger.warning(message);
    }

    public void info(String message) {
        logger.info(message);
    }

    public void debug(String message) {
        if (logger.isLoggable(Level.CONFIG))
            logger.info(String.format("[DEBUG] %s", message));
    }

}
