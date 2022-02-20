package com.gmail.nowyarek.pvpcontrol.components.api;

import com.gmail.nowyarek.pvpcontrol.PvPControlPlugin;
import com.gmail.nowyarek.pvpcontrol.components.logging.PluginLogger;
import com.gmail.nowyarek.pvpcontrol.components.plugin.PluginDisableEvent;
import com.gmail.nowyarek.pvpcontrol.components.plugin.PluginVersion;
import com.google.common.eventbus.Subscribe;
import org.bukkit.plugin.ServicePriority;

import javax.inject.Inject;
import java.util.EventListener;

public class PvPControlImplementation implements PvPControl, EventListener {
    private final PvPControlPlugin plugin;
    private final PluginLogger pluginLogger;
    private final String pluginVersion;

    @Inject
    public PvPControlImplementation(PvPControlPlugin plugin, PluginLogger pluginLogger, @PluginVersion String pluginVersion) {
        this.plugin = plugin;
        this.pluginLogger = pluginLogger;
        this.pluginVersion = pluginVersion;

        plugin.getEventBus().register(this);
        this.register();
    }

    @Override
    public String getVersion() {
        return this.pluginVersion;
    }

    private void register() {
        this.pluginLogger.debug("Registered API.");
        this.plugin.getServer().getServicesManager().register(PvPControl.class, this, this.plugin, ServicePriority.Normal);
        PvPControlProvider.register(this);
    }

    private void unregister() {
        this.pluginLogger.debug("Unregistered API.");
        PvPControlProvider.unregister();
        this.plugin.getServer().getServicesManager().unregister(PvPControl.class, this);
    }

    @Subscribe
    void onPluginDisable(PluginDisableEvent e) {
        this.unregister();
    }

}
