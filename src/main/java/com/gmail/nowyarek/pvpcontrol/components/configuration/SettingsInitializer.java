package com.gmail.nowyarek.pvpcontrol.components.configuration;

import com.gmail.nowyarek.pvpcontrol.PVPControl;
import com.gmail.nowyarek.pvpcontrol.components.logging.PluginLogger;
import com.gmail.nowyarek.pvpcontrol.components.plugin.PluginEnableEvent;
import com.google.common.eventbus.Subscribe;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class SettingsInitializer {
    final PVPControl plugin;
    final Settings settings;
    final PluginLogger logger;

    @Inject
    SettingsInitializer(PVPControl plugin, Settings settings, PluginLogger logger) {
        this.plugin = plugin;
        this.settings = settings;
        this.logger = logger;
        this.plugin.getEventBus().register(this);
    }

    @Subscribe
    void onPluginEnable(PluginEnableEvent ignoredE) throws Exception {
        try {
            System.out.println("SettingsInitializer#onPluginEnable thread: " + Thread.currentThread().getName());
            this.settings.initialize().get();
        }  catch (Exception ex) {
            throw new Exception("Settings initialization exception", ex);
        }
    }

}
