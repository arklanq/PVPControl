package com.gmail.nowyarek.pvpcontrol.components.plugin;

import org.bukkit.plugin.java.JavaPlugin;

public class PluginEnableEvent {
    private final JavaPlugin plugin;

    public PluginEnableEvent(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }
}