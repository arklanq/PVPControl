package com.gmail.nowyarek.pvpcontrol.components.configuration;

import com.gmail.nowyarek.pvpcontrol.components.logging.PluginLogger;
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

    public ConfigWithDefaults(JavaPlugin plugin, PluginLogger logger, String fileName) {
        super(plugin, logger, fileName);
    }

    @Override
    public CompletableFuture<Void> initialize() {
        return super.initialize()
            .exceptionally((Throwable e) -> {
                if (e instanceof CompletionException) {
                    logger.error("File `settings.yml` seems to be corrupted, plugin cannot read data from it. Look at the stack trace below to resolve the problem. If problem persists try to remove file `settings.yml` and let plugin generate it afresh. At the moment PVPControl will fallback to default configuration.");
                    e.getCause().printStackTrace();
                    isConfigFileCorrupted.set(true);
                    return null;
                }

                throw new RuntimeException("Forwarded exception from `exceptionally` clause.", e);
            })
            .thenCompose((Void value) -> this.initializeDefaults());
    }

    protected CompletableFuture<Void> initializeDefaults() {
        return CompletableFuture.supplyAsync(() -> {
            this.defaultsConfiguration = loadDefaultConfiguration("/" + fileName);
            if(this.isConfigFileCorrupted.get()) this.configuration = this.defaultsConfiguration;
            return null;
        });
    }

    private YamlConfiguration loadDefaultConfiguration(String internalFilePath) {
        InputStream is = getClass().getResourceAsStream(internalFilePath);
        Objects.requireNonNull(is, "InputStream to a config file resource is null.");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        return YamlConfiguration.loadConfiguration(br);
    }

}
