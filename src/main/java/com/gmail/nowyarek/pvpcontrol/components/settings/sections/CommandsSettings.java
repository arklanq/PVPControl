package com.gmail.nowyarek.pvpcontrol.components.settings.sections;

import com.gmail.nowyarek.pvpcontrol.components.configuration.ConfigurationValidation;
import com.gmail.nowyarek.pvpcontrol.components.settings.AbstractSettingsSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class CommandsSettings extends AbstractSettingsSection {
    private boolean blockAll;
    private List<String> whitelist;
    private List<String> blacklist;
    private List<String> executedWhenLogoutDuringPVP;

    CommandsSettings(FileConfiguration config, FileConfiguration defaultConfig) {
        super(config, defaultConfig);
    }

    @Override
    public ConfigurationValidation init() {
        ConfigurationValidation configuration = new ConfigurationValidation(this.config, this.defaultConfig);

        this.blockAll = configuration.requireBoolean("Commands.blockAll");
        this.whitelist = configuration.requireStringList("Commands.whitelist");
        this.blacklist = configuration.requireStringList("Commands.blacklist");
        this.executedWhenLogoutDuringPVP = configuration.requireStringList("Commands.executedWhenLogoutDuringPVP");

        return configuration;
    }

    public boolean areAllBlocked() {
        return blockAll;
    }

    public void setAllBlocked(boolean blockAll) {
        this.blockAll = blockAll;
    }

    public List<String> getWhitelist() {
        return whitelist;
    }

    public void setWhitelist(List<String> whitelist) {
        this.whitelist = whitelist;
    }

    public List<String> getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(List<String> blacklist) {
        this.blacklist = blacklist;
    }

    public List<String> getExecutedWhenLogoutDuringPVP() {
        return executedWhenLogoutDuringPVP;
    }

    public void setExecutedWhenLogoutDuringPVP(List<String> executedWhenLogoutDuringPVP) {
        this.executedWhenLogoutDuringPVP = executedWhenLogoutDuringPVP;
    }
}
