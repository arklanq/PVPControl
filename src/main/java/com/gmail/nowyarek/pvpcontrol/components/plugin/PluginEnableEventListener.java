package com.gmail.nowyarek.pvpcontrol.components.plugin;

import com.gmail.nowyarek.pvpcontrol.components.logging.PluginLogger;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Stage;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.EventListener;

public class PluginEnableEventListener implements EventListener {
    private final JavaPlugin plugin;
    private final Stage stage;
    private final PluginLogger logger;

    @Inject
    public PluginEnableEventListener(
        JavaPlugin plugin,
        Stage stage,
        PluginLogger logger
    ) {
        this.plugin = plugin;
        this.stage = stage;
        this.logger = logger;
    }

    @Subscribe
    public void onEvent(PluginEnableEvent e) {
        logger.debug("Saving default config...");
        plugin.saveDefaultConfig(); // Exceptions are already handed by this method

        logger.info(
            String.format("Enabled%s.", stage == Stage.DEVELOPMENT ? String.format(" [%s]", stage) : "")
        );
    }

}
