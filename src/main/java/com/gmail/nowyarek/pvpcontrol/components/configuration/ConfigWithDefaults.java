package com.gmail.nowyarek.pvpcontrol.components.configuration;

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
    public AtomicBoolean isConfigFileCorrupted = new AtomicBoolean(false);
    public AtomicBoolean isConfigurationMismatch = new AtomicBoolean(false);

    public ConfigWithDefaults(JavaPlugin plugin, PluginLogger logger, String fileName) {
        super(plugin, logger, fileName);
    }

    @Override
    public CompletableFuture<Void> initialize() {
        return super.initialize()
            .exceptionally((Throwable e) -> {
                if (e instanceof CompletionException) {
                    logger.error("File `settings.yml` seems to be corrupted, plugin cannot read data from it. Look at the stack trace below to resolve the problem. If problem persists try to remove file `settings.yml` and let plugin generate it afresh. At the moment PVPControl will fallback to default configuration.");
                    return null;
                }

                throw new RuntimeException("Forwarded exception from `exceptionally` clause.", e);
            })
            .thenCompose((Void value) -> this.initializeDefaults())
            .thenAccept((Void value) -> {
                if (this.isConfigurationMismatch.get())
                    this.logger.warn(String.format("You are working on an old config. Please remove config named `%s` and let plugin generate it afresh for full plugin functionality.", fileName));
            });
    }

    protected CompletableFuture<Void> initializeDefaults() {
        return CompletableFuture.supplyAsync(() -> {
            this.defaultsConfiguration = loadDefaultConfiguration("/" + fileName);
            this.isConfigurationMismatch.set(ensureConfigurationCompleteness(configuration, defaultsConfiguration));
            return null;
        });
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
