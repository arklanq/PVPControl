package com.gmail.nowyarek.pvpcontrol.configuration;

import com.gmail.nowyarek.pvpcontrol.PVPControl;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigBase {
    protected final PVPControl plugin;
    protected final File dataFolder;
    public final String configName;
    public FileConfiguration configuration;

    public ConfigBase(PVPControl plugin, String configName) {
        this.plugin = plugin;
        this.dataFolder = plugin.getDataFolder();
        this.configName = configName;
    }

    public FileConfiguration getConfig() {
        return this.configuration;
    }

    public void initialize() {
        File configFile = new File(dataFolder, this.configName);
        if (!configFile.exists()) {
            plugin.saveResource(this.configName, false);
        }
        this.configuration = YamlConfiguration.loadConfiguration(configFile);
    }

    // Alias
    public void reload() {
        this.initialize();
    }

}
