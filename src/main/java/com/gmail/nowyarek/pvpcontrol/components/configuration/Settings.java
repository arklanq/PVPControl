package com.gmail.nowyarek.pvpcontrol.components.configuration;

import co.aikar.taskchain.TaskChainFactory;
import com.gmail.nowyarek.pvpcontrol.components.logging.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;

@Singleton
public class Settings {
    private final ConfigBase config;

    @Inject
    public Settings(JavaPlugin plugin, PluginLogger logger, TaskChainFactory taskChainFactory) {
        this.config = new ConfigWithDefaults(plugin, logger, taskChainFactory, "config.yml");
    }

    CompletableFuture<Void> initialize() {
        System.out.println("Settings#initialize thread: " + Thread.currentThread().getName());
        return config.initialize();
    }

}
