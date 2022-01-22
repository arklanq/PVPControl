package com.gmail.nowyarek.pvpcontrol.components.settings;

import org.bukkit.configuration.file.FileConfiguration;

public abstract class AbstractSettingsSection implements SettingsSection {
    protected final FileConfiguration config;
    protected final FileConfiguration defaultConfig;

    AbstractSettingsSection(FileConfiguration config, FileConfiguration defaultConfig) {
        this.config = config;
        this.defaultConfig = defaultConfig;
    }

}
