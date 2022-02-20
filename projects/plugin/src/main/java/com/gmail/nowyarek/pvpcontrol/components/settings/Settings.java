package com.gmail.nowyarek.pvpcontrol.components.settings;

import com.gmail.nowyarek.pvpcontrol.components.configuration.ConfigurationValidation;
import org.bukkit.configuration.file.FileConfiguration;

import javax.inject.Singleton;
import java.util.stream.Stream;

@Singleton
public class Settings extends AbstractSettingsSection {
    private GeneralSettings General;
    private PVPSettings PVP;
    private CommandsSettings Commands;
    private IntegrationsSettings Integrations;

    Settings(FileConfiguration config, FileConfiguration defaultConfig) {
        super(config, defaultConfig);
    }

    @Override
    ConfigurationValidation init() {
        ConfigurationValidation configuration = new ConfigurationValidation(this.config, this.defaultConfig);

        this.General = new GeneralSettings(this.config, this.defaultConfig);
        this.PVP = new PVPSettings(this.config, this.defaultConfig);
        this.Commands = new CommandsSettings(this.config, this.defaultConfig);
        this.Integrations = new IntegrationsSettings(this.config, this.defaultConfig);

        Stream.of(General, PVP, Commands, Integrations)
            .map(AbstractSettingsSection::init)
            .map(ConfigurationValidation::getViolations)
            .forEach(configuration::mergeViolations);

        return configuration;
    }

    public GeneralSettings General() {
        return General;
    }

    public PVPSettings PVP() {
        return PVP;
    }

    public CommandsSettings Commands() {
        return Commands;
    }

    public IntegrationsSettings Integrations() {
        return Integrations;
    }
}
