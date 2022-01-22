package com.gmail.nowyarek.pvpcontrol.components.settings;

import com.gmail.nowyarek.pvpcontrol.components.configuration.ConfigurationValidation;
import org.bukkit.configuration.file.FileConfiguration;

public class GeneralSettings extends AbstractSettingsSection {
    private final String[] allowedLanguages = new String[] {"EN", "PL"};

    public String language;
    public int configVersion;

    GeneralSettings(FileConfiguration config, FileConfiguration defaultConfig) {
        super(config, defaultConfig);
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getConfigVersion() {
        return configVersion;
    }

    public void setConfigVersion(int configVersion) {
        this.configVersion = configVersion;
    }

    @Override
    public ConfigurationValidation init() {
        ConfigurationValidation configuration = new ConfigurationValidation(this.config, this.defaultConfig);

        this.language = configuration.requireStringEnum("General.language", allowedLanguages);
        this.configVersion = configuration.requireInt("General.configVersion");

        return configuration;
    }
}
