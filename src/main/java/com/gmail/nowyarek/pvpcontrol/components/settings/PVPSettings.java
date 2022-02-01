package com.gmail.nowyarek.pvpcontrol.components.settings;

import com.gmail.nowyarek.pvpcontrol.components.configuration.ConfigurationValidation;
import org.bukkit.configuration.file.FileConfiguration;

public class PVPSettings extends AbstractSettingsSection {
    private int timeInPVPMode;
    private boolean turnOffFlyOnPVP;
    private boolean disableInvisibilityOnPVP;
    private boolean blockChorusFruitsTeleport;
    private boolean blockEnderPeralsTeleport;
    private boolean killWhenLogoutDuringPVP;
    private boolean broadcastPlayerKilledInfo;

    PVPSettings(FileConfiguration config, FileConfiguration defaultConfig) {
        super(config, defaultConfig);
    }

    @Override
    ConfigurationValidation init() {
        ConfigurationValidation configuration = new ConfigurationValidation(this.config, this.defaultConfig);

        this.timeInPVPMode = configuration.requireInt("PVP.timeInPVPMode");
        this.turnOffFlyOnPVP = configuration.requireBoolean("PVP.turnOffFlyOnPVP");
        this.blockEnderPeralsTeleport = configuration.requireBoolean("PVP.blockEnderPeralsTeleport");
        this.blockChorusFruitsTeleport = configuration.requireBoolean("PVP.blockChorusFruitsTeleport");
        this.blockChorusFruitsTeleport = configuration.requireBoolean("PVP.blockChorusFruitsTeleport");
        this.killWhenLogoutDuringPVP = configuration.requireBoolean("PVP.killWhenLogoutDuringPVP");
        this.broadcastPlayerKilledInfo = configuration.requireBoolean("PVP.broadcastPlayerKilledInfo");

        return configuration;
    }

    public int getTimeInPVPMode() {
        return timeInPVPMode;
    }

    public void setTimeInPVPMode(int timeInPVPMode) {
        this.timeInPVPMode = timeInPVPMode;
    }

    public boolean isTurnOffFlyOnPVP() {
        return turnOffFlyOnPVP;
    }

    public void setTurnOffFlyOnPVP(boolean turnOffFlyOnPVP) {
        this.turnOffFlyOnPVP = turnOffFlyOnPVP;
    }

    public boolean isDisableInvisibilityOnPVP() {
        return disableInvisibilityOnPVP;
    }

    public void setDisableInvisibilityOnPVP(boolean disableInvisibilityOnPVP) {
        this.disableInvisibilityOnPVP = disableInvisibilityOnPVP;
    }

    public boolean isBlockChorusFruitsTeleport() {
        return blockChorusFruitsTeleport;
    }

    public void setBlockChorusFruitsTeleport(boolean blockChorusFruitsTeleport) {
        this.blockChorusFruitsTeleport = blockChorusFruitsTeleport;
    }

    public boolean isBlockEnderPeralsTeleport() {
        return blockEnderPeralsTeleport;
    }

    public void setBlockEnderPeralsTeleport(boolean blockEnderPeralsTeleport) {
        this.blockEnderPeralsTeleport = blockEnderPeralsTeleport;
    }

    public boolean isKillWhenLogoutDuringPVP() {
        return killWhenLogoutDuringPVP;
    }

    public void setKillWhenLogoutDuringPVP(boolean killWhenLogoutDuringPVP) {
        this.killWhenLogoutDuringPVP = killWhenLogoutDuringPVP;
    }

    public boolean isBroadcastPlayerKilledInfo() {
        return broadcastPlayerKilledInfo;
    }

    public void setBroadcastPlayerKilledInfo(boolean broadcastPlayerKilledInfo) {
        this.broadcastPlayerKilledInfo = broadcastPlayerKilledInfo;
    }
}
