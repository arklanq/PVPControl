package com.gmail.nowyarek.pvpcontrol.configuration;

import com.gmail.nowyarek.pvpcontrol.PVPControl;
import org.bukkit.configuration.InvalidConfigurationException;
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

    public void initialize() throws InvalidConfigurationException {
        File configFile = new File(dataFolder, this.configName);
        if (!configFile.exists()) {
            plugin.saveResource(this.configName, false);
        }
        configuration = YamlConfiguration.loadConfiguration(configFile);
        if(configuration.getKeys(false).size() == 0) {
            // empty file caused by InvalidConfigurationException
            throw new InvalidConfigurationException();
        }
    }

    // Alias
    public void reload() throws InvalidConfigurationException {
        this.initialize();
    }

}
