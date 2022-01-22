package com.gmail.nowyarek.pvpcontrol.components.settings;

import com.gmail.nowyarek.pvpcontrol.components.configuration.ConfigWithDefaults;
import com.gmail.nowyarek.pvpcontrol.components.configuration.ConfigurationValidation;
import com.gmail.nowyarek.pvpcontrol.components.configuration.ConfigurationValidationException;

public class GeneralSettings implements SettingsSection {
    private final ConfigWithDefaults config;
    private final String[] allowedLanguages = new String[] {"EN", "PL"};

    public String language;
    public int configVersion;

    GeneralSettings(ConfigWithDefaults config) {
        this.config = config;
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
    public void init() throws ConfigurationValidationException {
        ConfigurationValidation configuration = new ConfigurationValidation(this.config.getFileConfiguration());
        this.language = configuration.requireStringEnum("General.language", allowedLanguages);
        this.configVersion = configuration.requireInt("General.configVersion");
    }
}
