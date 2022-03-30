package com.gmail.nowyarek.pvpcontrol.components.settings;

import com.gmail.nowyarek.pvpcontrol.components.configuration.ConfigurationValidation;
import org.bukkit.configuration.file.FileConfiguration;

import javax.inject.Singleton;
import java.util.stream.Stream;

@Singleton
public class Settings extends AbstractSettingsSection {
    private GeneralSettings General;
    private PvPSettings PvP;
    private FeaturesSettings Features;
    private CommandsSettings Commands;
    private IntegrationsSettings Integrations;

    Settings(FileConfiguration config, FileConfiguration defaultConfig) {
        super(config, defaultConfig);
    }

    @Override
    ConfigurationValidation init() {
        ConfigurationValidation configuration = new ConfigurationValidation(this.config, this.defaultConfig);

        this.General = new GeneralSettings(this.config, this.defaultConfig);
        this.PvP = new PvPSettings(this.config, this.defaultConfig);
        this.Features = new FeaturesSettings(this.config, this.defaultConfig);
        this.Commands = new CommandsSettings(this.config, this.defaultConfig);
        this.Integrations = new IntegrationsSettings(this.config, this.defaultConfig);

        Stream.of(General, PvP, Features, Commands, Integrations)
            .map(AbstractSettingsSection::init)
            .map(ConfigurationValidation::getViolations)
            .forEach(configuration::mergeViolations);

        return configuration;
    }

    public GeneralSettings General() {
        return General;
    }

    public PvPSettings PvP() {
        return PvP;
    }

    public FeaturesSettings Features() {
        return Features;
    }

    public CommandsSettings Commands() {
        return Commands;
    }

    public IntegrationsSettings Integrations() {
        return Integrations;
    }
}
