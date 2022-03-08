package com.gmail.nowyarek.pvpcontrol.components.settings;

import com.gmail.nowyarek.pvpcontrol.components.configuration.ConfigurationValidation;
import org.bukkit.configuration.file.FileConfiguration;

public class DamagerSettings extends AbstractSettingsSection {
    private boolean projectiles;
    private boolean NPCs;

    DamagerSettings(FileConfiguration config, FileConfiguration defaultConfig) {
        super(config, defaultConfig);
    }

    @Override
    ConfigurationValidation init() {
        ConfigurationValidation configuration = new ConfigurationValidation(this.config, this.defaultConfig);

        this.projectiles = configuration.requireBoolean("PvP.Damager.projectiles");
        this.NPCs = configuration.requireBoolean("PvP.Damager.NPCs");

        return configuration;
    }

    public boolean areProjectilesPermitted() {
        return projectiles;
    }

    public boolean areNPCsPermitted() {
        return NPCs;
    }
}
