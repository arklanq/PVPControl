package com.gmail.nowyarek.pvpcontrol.configuration;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.*;

public class ConfigurationUtils {

    public static YamlConfiguration loadDefaultConfiguration(@NotNull String internalFilePath) {
        InputStream is = ConfigurationUtils.class.getResourceAsStream(internalFilePath);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        return YamlConfiguration.loadConfiguration(br);
    }

    public static boolean ensureConfigurationCompleteness(@NotNull FileConfiguration config, @NotNull FileConfiguration defaults) {
        return ConfigurationUtils.checkAllSubsections(config, defaults, false);
    }

    private static boolean checkAllSubsections(@NotNull ConfigurationSection file, @NotNull ConfigurationSection defaults, boolean configMismatch) {
        for(String key : defaults.getKeys(false)){
            if(!defaults.isConfigurationSection(key)){
                if(defaults.isSet(key)){
                    if(!file.isSet(key)){
                        file.set(key, defaults.get(key));
                        configMismatch = true;
                    }
                }
            } else {
                ConfigurationSection fileCS = file.getConfigurationSection(key);
                if(fileCS == null){
                    fileCS = file.createSection(key);
                    configMismatch = true;
                }
                ConfigurationSection defaultsCS = defaults.getConfigurationSection(key);
                assert defaultsCS != null;
                configMismatch = ConfigurationUtils.checkAllSubsections(fileCS, defaultsCS, configMismatch);
            }
        }
        return configMismatch;
    }

}
