package com.gmail.nowyarek.pvpcontrol.components.settings;

import com.gmail.nowyarek.pvpcontrol.components.configuration.ConfigurationValidation;
import org.bukkit.configuration.file.FileConfiguration;

public abstract class AbstractSettingsSection {
    protected final FileConfiguration config;
    protected final FileConfiguration defaultConfig;

    AbstractSettingsSection(FileConfiguration config, FileConfiguration defaultConfig) {
        this.config = config;
        this.defaultConfig = defaultConfig;
    }

    abstract ConfigurationValidation init();

}
