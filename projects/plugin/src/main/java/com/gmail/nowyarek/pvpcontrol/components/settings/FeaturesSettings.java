package com.gmail.nowyarek.pvpcontrol.components.settings;

import com.gmail.nowyarek.pvpcontrol.components.configuration.ConfigurationValidation;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.stream.Stream;

public class FeaturesSettings extends AbstractSettingsSection {
    private BossbarSettings Bossbar;
    private AdminProtectionSettings AdminProtection;

    FeaturesSettings(FileConfiguration config, FileConfiguration defaultConfig) {
        super(config, defaultConfig);
    }

    @Override
    ConfigurationValidation init() {
        ConfigurationValidation configuration = new ConfigurationValidation(this.config, this.defaultConfig);

        this.Bossbar = new BossbarSettings(this.config, this.defaultConfig);
        this.AdminProtection = new AdminProtectionSettings(this.config, this.defaultConfig);

        Stream.of(Bossbar, AdminProtection)
            .map(AbstractSettingsSection::init)
            .map(ConfigurationValidation::getViolations)
            .forEach(configuration::mergeViolations);

        return configuration;
    }

    public BossbarSettings Bossbar() {
        return Bossbar;
    }

    public AdminProtectionSettings AdminProtection() {
        return AdminProtection;
    }
}
