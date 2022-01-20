package com.gmail.nowyarek.pvpcontrol.components.TaskChain;

import co.aikar.taskchain.BukkitTaskChainFactory;
import co.aikar.taskchain.TaskChainFactory;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class TaskChainFactoryProvider implements Provider<TaskChainFactory> {
    private final JavaPlugin plugin;

    @Inject
    public TaskChainFactoryProvider(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public TaskChainFactory get() {
        return BukkitTaskChainFactory.create(plugin);
    }

}
