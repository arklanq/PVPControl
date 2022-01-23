package com.gmail.nowyarek.pvpcontrol.components.settings;

import com.gmail.nowyarek.pvpcontrol.components.configuration.ConfigurationValidation;
import org.bukkit.configuration.file.FileConfiguration;

public class IntegrationsSettings extends AbstractSettingsSection {
    public EssentialsXSettings EssentialsX;

    IntegrationsSettings(FileConfiguration config, FileConfiguration defaultConfig) {
        super(config, defaultConfig);
    }

    @Override
    public ConfigurationValidation init() {
        ConfigurationValidation configuration = new ConfigurationValidation(this.config, this.defaultConfig);

        this.EssentialsX = new EssentialsXSettings(this.config, this.defaultConfig);
        configuration.mergeViolations(EssentialsX.init().getViolations());

        return configuration;
    }


}
