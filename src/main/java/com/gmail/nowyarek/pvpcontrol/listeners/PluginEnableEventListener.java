package com.gmail.nowyarek.pvpcontrol.listeners;

import com.gmail.nowyarek.pvpcontrol.annotations.PluginLogger;
import com.gmail.nowyarek.pvpcontrol.events.PluginEnableEvent;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Stage;

import java.util.EventListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PluginEnableEventListener implements EventListener {
    private final Stage stage;
    private final Logger logger;

    @Inject
    public PluginEnableEventListener(Stage stage, @PluginLogger Logger logger) {
        this.stage = stage;
        this.logger = logger;
    }

    @Subscribe
    public void onEvent(PluginEnableEvent e) {
        logger.log(
            Level.INFO,
            String.format("Enabled%s.", stage == Stage.DEVELOPMENT ? String.format(" [%s]", stage) : "")
        );
    }

}
