package com.gmail.nowyarek.pvpcontrol.components.configuration;

import com.gmail.nowyarek.pvpcontrol.components.logging.PluginLogger;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class ConfigBase {
    protected final JavaPlugin plugin;
    protected final PluginLogger logger;
    protected final File dataFolder;
    public final String fileName;
    public FileConfiguration configuration;

    public ConfigBase(JavaPlugin plugin, PluginLogger logger, File dataFolder, String fileName) {
        this.plugin = plugin;
        this.logger = logger;
        this.dataFolder = dataFolder;
        this.fileName = fileName;
    }

    public FileConfiguration getFileConfiguration() {
        return this.configuration;
    }

    public CompletableFuture<Void> initialize() {
        return CompletableFuture.supplyAsync(() -> {
            File configFile = new File(dataFolder, this.fileName);

            if (!configFile.exists())
                plugin.saveResource(this.fileName, false);

            try {
                configuration = this.loadConfiguration(configFile);
            } catch (Exception e) {
                throw new CompletionException(e);
            }

            return null;
        });
    }

    // Alias
    public void reload() {
        this.initialize();
    }

    private YamlConfiguration loadConfiguration(File file) throws IOException, InvalidConfigurationException {
        YamlConfiguration config = new YamlConfiguration();
        config.load(file);
        return config;
    }

}
