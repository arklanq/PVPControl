package com.gmail.nowyarek.pvpcontrol.components.plugin;

import org.bukkit.plugin.java.JavaPlugin;

public class PluginDisabledEvent {
    private final JavaPlugin plugin;

    public PluginDisabledEvent(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }
}
