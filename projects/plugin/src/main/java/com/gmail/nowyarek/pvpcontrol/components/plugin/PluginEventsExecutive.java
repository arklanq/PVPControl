package com.gmail.nowyarek.pvpcontrol.components.plugin;

import com.gmail.nowyarek.pvpcontrol.PvPControlPlugin;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class PluginEventsExecutive {
    private final PvPControlPlugin plugin;

    @Inject
    public PluginEventsExecutive(
        PvPControlPlugin plugin,
        PluginEnableEventListener enableEventListener,
        PluginDisableEventListener disableEventListener
    ) {
        this.plugin = plugin;
        this.plugin.getEventBus().register(enableEventListener);
        this.plugin.getEventBus().register(disableEventListener);
    }

    public void postEnableEvent() {
        this.plugin.getEventBus().post(new PluginEnableEvent(this.plugin));
    }

    public void postDisableEvent() {
        this.plugin.getEventBus().post(new PluginDisableEvent(this.plugin));
    }
}
