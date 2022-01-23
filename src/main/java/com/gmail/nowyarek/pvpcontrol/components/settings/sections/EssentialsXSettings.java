package com.gmail.nowyarek.pvpcontrol.components.settings.sections;

import com.gmail.nowyarek.pvpcontrol.components.configuration.ConfigurationValidation;
import com.gmail.nowyarek.pvpcontrol.components.settings.AbstractSettingsSection;
import org.bukkit.configuration.file.FileConfiguration;

public class EssentialsXSettings extends AbstractSettingsSection {
    private boolean disableGodModeOnHit;

    EssentialsXSettings(FileConfiguration config, FileConfiguration defaultConfig) {
        super(config, defaultConfig);
    }

    @Override
    public ConfigurationValidation init() {
        ConfigurationValidation configuration = new ConfigurationValidation(this.config, this.defaultConfig);

        this.disableGodModeOnHit = configuration.requireBoolean("Integrations.EssentialsX.disableGodModeOnHit");

        return configuration;
    }

    public boolean isDisableGodModeOnHit() {
        return disableGodModeOnHit;
    }

    public void setDisableGodModeOnHit(boolean disableGodModeOnHit) {
        this.disableGodModeOnHit = disableGodModeOnHit;
    }
}
