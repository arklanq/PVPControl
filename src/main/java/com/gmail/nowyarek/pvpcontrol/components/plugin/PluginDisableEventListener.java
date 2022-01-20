package com.gmail.nowyarek.pvpcontrol.components.plugin;

import com.gmail.nowyarek.pvpcontrol.components.logging.PluginLogger;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import java.util.EventListener;

public class PluginDisableEventListener implements EventListener {
    private final PluginLogger logger;

    @Inject
    public PluginDisableEventListener(PluginLogger logger) {
        this.logger = logger;
    }

    @Subscribe
    public void onEvent(PluginDisableEvent e) {
        logger.info("Disabled.");
    }

}
