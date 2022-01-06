package com.gmail.nowyarek.pvpcontrol.modules;

import co.aikar.taskchain.BukkitTaskChainFactory;
import co.aikar.taskchain.TaskChainFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.bukkit.plugin.java.JavaPlugin;

public class TaskChainsConfigurationModule extends AbstractModule {

    @Provides
    public TaskChainFactory provideTaskChainFactory(JavaPlugin plugin) {
        return BukkitTaskChainFactory.create(plugin);
    }

}
