package com.gmail.nowyarek.pvpcontrol.events;

import org.bukkit.plugin.java.JavaPlugin;

public class PluginDisableEvent {
    private final JavaPlugin plugin;

    public PluginDisableEvent(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }
}
