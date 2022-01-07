package com.gmail.nowyarek.pvpcontrol.listeners;

import com.gmail.nowyarek.pvpcontrol.annotations.PluginLogger;
import com.gmail.nowyarek.pvpcontrol.events.PluginDisableEvent;
import com.gmail.nowyarek.pvpcontrol.providers.TaskChainFactoryProvider;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import java.util.EventListener;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PluginDisableEventListener implements EventListener {
    private final TaskChainFactoryProvider taskChainFactoryProvider;
    private final Logger logger;

    @Inject
    public PluginDisableEventListener(TaskChainFactoryProvider taskChainFactoryProvider, @PluginLogger Logger logger) {
        this.taskChainFactoryProvider = taskChainFactoryProvider;
        this.logger = logger;
    }

    @Subscribe
    public void onEvent(PluginDisableEvent e) {
        if (taskChainFactoryProvider.isCreated()) {
            this.taskChainFactoryProvider.get().shutdown(2500, TimeUnit.MILLISECONDS);
            this.logger.log(Level.FINE, "Shutting down Task Chain Factory");
        }

        logger.log(Level.INFO, "Disabled.");
    }

}
