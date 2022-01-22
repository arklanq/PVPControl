package com.gmail.nowyarek.pvpcontrol.components.configuration;

import com.gmail.nowyarek.pvpcontrol.components.logging.PluginLogger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public class ConfigBase {
    protected final JavaPlugin plugin;
    protected final PluginLogger logger;
    protected final File dataFolder;
    public final String fileName;
    public FileConfiguration configuration;

    public ConfigBase(JavaPlugin plugin, PluginLogger logger, String fileName) {
        this.plugin = plugin;
        this.logger = logger;
        this.dataFolder = plugin.getDataFolder();
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

            configuration = YamlConfiguration.loadConfiguration(configFile);

            return null;
        });
    }

    // Alias
    public void reload() {
        this.initialize();
    }

}
