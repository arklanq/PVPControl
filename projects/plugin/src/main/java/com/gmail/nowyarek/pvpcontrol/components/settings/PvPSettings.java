package com.gmail.nowyarek.pvpcontrol.components.settings;

import com.gmail.nowyarek.pvpcontrol.components.configuration.ConfigurationValidation;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.stream.Stream;

public class PvPSettings extends AbstractSettingsSection {
    private int combatDuration;
    private boolean disableFly;
    private boolean disableInvisibility;
    private boolean blockChorusFruitsTeleport;
    private boolean blockEnderPeralsTeleport;
    private boolean killOnLogout;
    private boolean broadcastPlayerKilledInfo;
    private DamagerSettings Damager;

    PvPSettings(FileConfiguration config, FileConfiguration defaultConfig) {
        super(config, defaultConfig);
    }

    @Override
    ConfigurationValidation init() {
        ConfigurationValidation configuration = new ConfigurationValidation(this.config, this.defaultConfig);

        this.combatDuration = configuration.requireInt("PvP.combatDuration");
        this.disableFly = configuration.requireBoolean("PvP.disableFly");
        this.disableInvisibility = configuration.requireBoolean("PvP.disableInvisibility");
        this.blockEnderPeralsTeleport = configuration.requireBoolean("PvP.blockEnderPeralsTeleport");
        this.blockChorusFruitsTeleport = configuration.requireBoolean("PvP.blockChorusFruitsTeleport");
        this.blockChorusFruitsTeleport = configuration.requireBoolean("PvP.blockChorusFruitsTeleport");
        this.killOnLogout = configuration.requireBoolean("PvP.killOnLogout");
        this.broadcastPlayerKilledInfo = configuration.requireBoolean("PvP.broadcastPlayerKilledInfo");

        this.Damager = new DamagerSettings(this.config, this.defaultConfig);

        Stream.of(Damager)
            .map(AbstractSettingsSection::init)
            .map(ConfigurationValidation::getViolations)
            .forEach(configuration::mergeViolations);

        return configuration;
    }

    public DamagerSettings Damager() {
        return Damager;
    }

    public int getCombatDuration() {
        return combatDuration;
    }

    public boolean isFlyPermitted() {
        return disableFly;
    }
    public boolean isInvisibilityPermitted() {
        return disableInvisibility;
    }

    public boolean isChorusFruitsTeleportPermitted() {
        return blockChorusFruitsTeleport;
    }

    public boolean isEnderPeralsTeleportPermitted() {
        return blockEnderPeralsTeleport;
    }

    public boolean shouldKillPlayerOnLogout() {
        return killOnLogout;
    }

    public boolean shouldBroadcastPlayerKilledInfo() {
        return broadcastPlayerKilledInfo;
    }
}
