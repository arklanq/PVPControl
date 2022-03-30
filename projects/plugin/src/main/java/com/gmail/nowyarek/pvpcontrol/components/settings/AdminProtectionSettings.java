package com.gmail.nowyarek.pvpcontrol.components.settings;

import com.gmail.nowyarek.pvpcontrol.components.configuration.ConfigurationValidation;
import org.bukkit.configuration.file.FileConfiguration;

public class AdminProtectionSettings extends AbstractSettingsSection {
    private boolean enabledByDefault;

    AdminProtectionSettings(FileConfiguration config, FileConfiguration defaultConfig) {
        super(config, defaultConfig);
    }

    @Override
    ConfigurationValidation init() {
        ConfigurationValidation configuration = new ConfigurationValidation(this.config, this.defaultConfig);

        this.enabledByDefault = configuration.requireBoolean("Features.AdminProtection.enabledByDefault");

        return configuration;
    }

    public boolean isEnabledByDefault() {
        return enabledByDefault;
    }
}
