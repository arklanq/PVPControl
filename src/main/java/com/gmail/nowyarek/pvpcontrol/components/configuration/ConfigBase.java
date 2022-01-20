package com.gmail.nowyarek.pvpcontrol.components.configuration;

import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainFactory;
import co.aikar.taskchain.TaskChainTasks.Task;
import com.gmail.nowyarek.pvpcontrol.components.TaskChain.TaskExecutionException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public class ConfigBase {
    protected final JavaPlugin plugin;
    protected final File dataFolder;
    public final String configName;
    public FileConfiguration configuration;
    public TaskChainFactory taskChainFactory;

    public ConfigBase(JavaPlugin plugin, TaskChainFactory taskChainFactory, String configName) {
        this.plugin = plugin;
        this.dataFolder = plugin.getDataFolder();
        this.taskChainFactory = taskChainFactory;
        this.configName = configName;
    }

    public FileConfiguration getFileConfiguration() {
        return this.configuration;
    }

    public CompletableFuture<Void> initialize() {
        CompletableFuture<Void> future = new CompletableFuture<>();
        TaskChain<?> chain = this.taskChainFactory.newChain()
            .async(() -> {
                System.out.println("ConfigBase#initialize -> task.async thread: " + Thread.currentThread().getName());
                File configFile = new File(dataFolder, this.configName);

                if (!configFile.exists())
                    plugin.saveResource(this.configName, false);

                configuration = YamlConfiguration.loadConfiguration(configFile);

                if (configuration.getKeys(false).size() == 0)
                    throw new InvalidConfigurationException(String.format("Loaded configuration file '%s' was empty.", configName));
            });

        chain.execute(
            (Boolean done) -> {
                System.out.println("ConfigBase#initialize -> task.onComplete thread: " + Thread.currentThread().getName());
                future.complete(null);
            },
            (Exception e, Task<?, ?> task) -> {
                System.out.println("ConfigBase#initialize -> task.onError thread: " + Thread.currentThread().getName());
                future.completeExceptionally(new TaskExecutionException(e, task));
            }
        );

        return future;
    }

    // Alias
    public void reload() {
        this.initialize();
    }

}
