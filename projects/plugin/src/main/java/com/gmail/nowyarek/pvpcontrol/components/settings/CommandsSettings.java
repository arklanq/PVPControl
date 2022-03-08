package com.gmail.nowyarek.pvpcontrol.components.settings;

import com.gmail.nowyarek.pvpcontrol.components.configuration.ConfigurationValidation;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class CommandsSettings extends AbstractSettingsSection {
    private boolean blockAll;
    private List<String> whitelist;
    private List<String> blacklist;
    private List<String> executedWhenLogout;

    CommandsSettings(FileConfiguration config, FileConfiguration defaultConfig) {
        super(config, defaultConfig);
    }

    @Override
    ConfigurationValidation init() {
        ConfigurationValidation configuration = new ConfigurationValidation(this.config, this.defaultConfig);

        this.blockAll = configuration.requireBoolean("Commands.blockAll");
        this.whitelist = configuration.requireStringList("Commands.whitelist");
        this.blacklist = configuration.requireStringList("Commands.blacklist");
        this.executedWhenLogout = configuration.requireStringList("Commands.executedWhenLogout");

        return configuration;
    }

    public boolean areAllBlocked() {
        return blockAll;
    }

    public List<String> getWhitelist() {
        return whitelist;
    }

    public List<String> getBlacklist() {
        return blacklist;
    }

    public List<String> getExecutedWhenLogout() {
        return executedWhenLogout;
    }
}
