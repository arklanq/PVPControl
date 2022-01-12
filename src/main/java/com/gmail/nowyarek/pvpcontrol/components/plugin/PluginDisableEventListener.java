package com.gmail.nowyarek.pvpcontrol.components.plugin;

import com.gmail.nowyarek.pvpcontrol.components.logging.PluginLogger;
import com.gmail.nowyarek.pvpcontrol.components.task_chain.TaskChainFactoryProvider;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import java.util.EventListener;
import java.util.concurrent.TimeUnit;

public class PluginDisableEventListener implements EventListener {
    private final PluginLogger logger;
    private final TaskChainFactoryProvider taskChainFactoryProvider;

    @Inject
    public PluginDisableEventListener(PluginLogger logger, TaskChainFactoryProvider taskChainFactoryProvider) {
        this.logger = logger;
        this.taskChainFactoryProvider = taskChainFactoryProvider;
    }

    @Subscribe
    public void onEvent(PluginDisableEvent e) {
        if (taskChainFactoryProvider.isCreated()) {
            this.taskChainFactoryProvider.get().shutdown(2500, TimeUnit.MILLISECONDS);
            this.logger.debug("Shutting down Task Chain Factory");
        }

        logger.info("Disabled.");
    }

}
