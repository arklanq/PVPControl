package com.gmail.nowyarek.pvpcontrol.components.settings;

import com.gmail.nowyarek.pvpcontrol.PvPControlPlugin;
import com.gmail.nowyarek.pvpcontrol.annotations.Blocking;
import com.gmail.nowyarek.pvpcontrol.components.logging.PluginLogger;
import com.gmail.nowyarek.pvpcontrol.models.EventSource;
import com.google.common.eventbus.EventBus;

import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;
import java.util.concurrent.CompletableFuture;

@Singleton
public class SettingsProvider implements Provider<Settings>, EventSource {
    private final EventBus eventBus = new EventBus();
    private final PvPControlPlugin plugin;
    private final PluginLogger logger;
    private final SettingsConstructor settingsConstructor;
    private volatile Settings settings;

    @Inject @Blocking
    public SettingsProvider(PvPControlPlugin plugin, PluginLogger logger, SettingsConstructor settingsConstructor) {
        this.plugin = plugin;
        this.logger = logger;
        this.settingsConstructor = settingsConstructor;

        this.initializeSync();
    }

    @Override
    public Settings get() {
        return this.settings;
    }

    /**
     * Available events:
     * <ul>
     *     <li>{@link SettingsLoadEvent}</li>
     * </ul>
     */
    @Override
    public EventBus getEventBus() {
        return this.eventBus;
    }

    public CompletableFuture<Void> reinitialize() {
        return this.initializeAsync();
    }

    private CompletableFuture<Void> initializeAsync() {
        return this.settingsConstructor.construct().thenAccept((Settings settings) -> {
            this.logger.debug("Settings object constructed.");
            this.eventBus.post(new SettingsLoadEvent(settings));
            this.settings = settings;
        });
    }

    @Blocking
    private void initializeSync() {
        try {
            this.initializeAsync().get();
        } catch (Exception e) {
            e.printStackTrace();
            this.plugin.onDisable();
        }
    }
}
