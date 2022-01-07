package com.gmail.nowyarek.pvpcontrol.listeners;

import com.gmail.nowyarek.pvpcontrol.annotations.PluginVersion;
import com.gmail.nowyarek.pvpcontrol.events.PluginEnableEvent;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import java.util.EventListener;

public class PluginEnableEventListener implements EventListener {
    private final String pluginVersion;

    @Inject
    public PluginEnableEventListener(@PluginVersion String pluginVersion) {
        this.pluginVersion = pluginVersion;
    }

    @Subscribe
    public void onEvent(PluginEnableEvent e) {
        e.getPlugin().getServer().getConsoleSender().sendMessage(
            String.format("§2%s v%s enabled.", e.getPlugin().getName(), pluginVersion)
        );
    }

}
