package com.gmail.nowyarek.pvpcontrol.components.configuration;

import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainFactory;
import co.aikar.taskchain.TaskChainTasks;
import com.gmail.nowyarek.pvpcontrol.components.TaskChain.TaskExecutionException;
import com.gmail.nowyarek.pvpcontrol.components.logging.PluginLogger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConfigWithDefaults extends ConfigBase {
    public YamlConfiguration defaultsConfiguration;
    public AtomicBoolean isConfigurationMismatch = new AtomicBoolean(false);
    private final PluginLogger logger;

    public ConfigWithDefaults(JavaPlugin plugin, PluginLogger logger, TaskChainFactory taskChainFactory, String configName) {
        super(plugin, taskChainFactory, configName);
        this.logger = logger;
    }

    @Override
    public CompletableFuture<Void> initialize() {
        CompletableFuture<Void> future = super.initialize();

        future = future.thenCompose((Void value) -> this.initializeDefaults());

        future = future.exceptionally((Throwable e) -> {
            System.out.println("ConfigWithDefault#initialize -> future.exceptionally thread: " + Thread.currentThread().getName());

            if(e instanceof CompletionException) {
                if (e.getCause() instanceof TaskExecutionException) {
                    if (e.getCause().getCause() instanceof InvalidConfigurationException) {
                        this.initializeDefaults().join();
                        configuration = defaultsConfiguration;
                        logger.error("File `config.yml` seems to be corrupted, plugin cannot read data from it. Look at the stack trace above to resolve the problem. If problem persists try to remove file `config.yml` and let plugin generate it afresh. At the moment PVPControl will fallback to default configuration.");
                        return null;
                    }
                }
            }

            throw new RuntimeException("Forwarded exception from `exceptionally` clause.", e);
        });

        future = future.thenAccept((Void value) -> {
            System.out.println("ConfigWithDefault#initialize -> future.thenAccept thread: " + Thread.currentThread().getName());
            if (this.isConfigurationMismatch.get())
                this.logger.warn(String.format("You are working on an old config. Please remove config named `%s` and let plugin generate it afresh for full plugin functionality.", configName));
        });

        return future;
    }

    protected CompletableFuture<Void> initializeDefaults() {
        CompletableFuture<Void> future = new CompletableFuture<>();

        TaskChain<?> chain = this.taskChainFactory.newChain()
            .async(() -> {
                System.out.println("ConfigWithDefaults#initializeDefaults -> task.async thread: " + Thread.currentThread().getName());
                defaultsConfiguration = loadDefaultConfiguration("/" + configName);
                isConfigurationMismatch.set(ensureConfigurationCompleteness(configuration, defaultsConfiguration));
            });

        chain.execute(
            (Boolean done) -> {
                System.out.println("ConfigWithDefaults#initialize -> task.onComplete thread: " + Thread.currentThread().getName());
                future.complete(null);
            },
            (Exception e, TaskChainTasks.Task<?, ?> task) -> {
                System.out.println("ConfigWithDefaults#initialize -> task.onError thread: " + Thread.currentThread().getName());
                future.completeExceptionally(new TaskExecutionException(e, task));
            }
        );

        return future;
    }

    private YamlConfiguration loadDefaultConfiguration(String internalFilePath) {
        InputStream is = getClass().getResourceAsStream(internalFilePath);
        Objects.requireNonNull(is, "InputStream to a config file resource is null.");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        return YamlConfiguration.loadConfiguration(br);
    }

    private boolean ensureConfigurationCompleteness(FileConfiguration config, FileConfiguration defaults) {
        return checkAllSubsections(config, defaults, false);
    }

    private boolean checkAllSubsections(ConfigurationSection file, ConfigurationSection defaults, boolean configMismatch) {
        for (String key : defaults.getKeys(false)) {
            if (!defaults.isConfigurationSection(key)) {
                if (defaults.isSet(key)) {
                    if (!file.isSet(key)) {
                        file.set(key, defaults.get(key));
                        configMismatch = true;
                    }
                }
            } else {
                ConfigurationSection fileCS = file.getConfigurationSection(key);
                if (fileCS == null) {
                    fileCS = file.createSection(key);
                    configMismatch = true;
                }
                ConfigurationSection defaultsCS = defaults.getConfigurationSection(key);
                assert defaultsCS != null;
                configMismatch = checkAllSubsections(fileCS, defaultsCS, configMismatch);
            }
        }
        return configMismatch;
    }

}
