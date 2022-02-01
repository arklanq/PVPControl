package com.gmail.nowyarek.pvpcontrol.components.plugin;

import org.bukkit.plugin.java.JavaPlugin;

public class PluginEnabledEvent {
    private final JavaPlugin plugin;

    public PluginEnabledEvent(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }
}
