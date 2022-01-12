package com.gmail.nowyarek.pvpcontrol.components.task_chain;

import co.aikar.taskchain.BukkitTaskChainFactory;
import co.aikar.taskchain.TaskChainFactory;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class TaskChainFactoryProvider implements Provider<TaskChainFactory> {
    private final JavaPlugin plugin;
    @Nullable
    private TaskChainFactory taskChainFactory = null;
    public static int debug = 0;

    @Inject
    public TaskChainFactoryProvider(JavaPlugin plugin) {
        this.plugin = plugin;
        debug++;
    }

    @Override
    public TaskChainFactory get() {
        if(taskChainFactory == null) taskChainFactory = BukkitTaskChainFactory.create(plugin);
        return taskChainFactory;
    }

    public boolean isCreated() {
        return taskChainFactory != null;
    }

}
