package com.gmail.nowyarek.pvpcontrol.components.settings;

import com.gmail.nowyarek.pvpcontrol.PVPControl;
import com.gmail.nowyarek.pvpcontrol.annotations.Blocking;
import com.gmail.nowyarek.pvpcontrol.components.logging.PluginLogger;
import com.gmail.nowyarek.pvpcontrol.components.plugin.PluginEnableEvent;
import com.gmail.nowyarek.pvpcontrol.models.EventsSource;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.EventListener;

@Singleton
public class SettingsProvider implements Provider<Settings>, EventListener, EventsSource {
    private final EventBus eventBus = new EventBus();
    private final PVPControl plugin;
    private final PluginLogger logger;
    private final Provider<SettingsConstructor> settingsConstructorProvider;
    @Nullable
    private Settings settings;

    @Inject
    public SettingsProvider(PVPControl plugin, PluginLogger logger, Provider<SettingsConstructor> settingsConstructorProvider) {
        this.plugin = plugin;
        this.logger = logger;
        this.settingsConstructorProvider = settingsConstructorProvider;

        plugin.getEventBus().register(this);
    }

    @Override
    public Settings get() {
        return this.settings;
    }

    @Subscribe
    @Blocking
    public void onPluginEnable(PluginEnableEvent ignoredEvent) {
        SettingsConstructor settingsConstructor = this.settingsConstructorProvider.get();
        try {
            this.settings = settingsConstructor.construct().get();
            this.logger.debug("Settings object constructed.");
            this.eventBus.post(new SettingsLoadEvent(this.settings));
        } catch (Exception e) {
            e.printStackTrace();
            this.plugin.onDisable();
        }
    }

    @Override
    public EventBus getEventBus() {
        return this.eventBus;
    }
}
