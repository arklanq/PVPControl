package com.gmail.nowyarek.pvpcontrol.components.settings;

import com.gmail.nowyarek.pvpcontrol.components.configuration.ConfigurationValidation;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.regex.Pattern;

public class GeneralSettings extends AbstractSettingsSection {
    private String language;
    private int configVersion;

    GeneralSettings(FileConfiguration config, FileConfiguration defaultConfig) {
        super(config, defaultConfig);
    }

    @Override
    ConfigurationValidation init() {
        ConfigurationValidation configuration = new ConfigurationValidation(this.config, this.defaultConfig);

        this.language = configuration.requireStringMatchingPattern(
            "General.language",
            Pattern.compile("^\\w{2}$"),
            "`{path}` should declare a valid ISO 639-1 language code (https://www.loc.gov/standards/iso639-2/php/code_list.php). Instead received: `{actualValue}`."
        );

        if(this.language != null) this.language = this.language.toLowerCase();

        this.configVersion = configuration.requireInt("General.configVersion");

        return configuration;
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
}
