package com.gmail.nowyarek.pvpcontrol.configs.settings;

import org.bukkit.configuration.ConfigurationSection;

import com.gmail.nowyarek.pvpcontrol.configs.CheckSectionExpeller;

public class PVP extends SettingsSubSection {
	private short timeInPVPMode;
	private boolean turnOffFlyOnPVP, disableInvisibilityOnPVP, blockAllCommandsOnPVP,
	blockAnyKindOfTeleportOnPVP, blockChorusFruitTeleport, blockEnderPeralTeleport, 
	killWhenLogoutOnPVP, broadcastPlayerLoggingOnPVP;
	
	public short getTimeInPVPMode() {
		return this.timeInPVPMode;
	}
	public boolean getTurnOffFlyOnPVP() {
		return this.turnOffFlyOnPVP;
	}
	public boolean getDisableInvisibilityOnPVP() {
		return this.disableInvisibilityOnPVP;
	}
	public boolean getBlockAllCommandsOnPVP() {
		return this.blockAllCommandsOnPVP;
	}
	public boolean getBlockAnyKindOfTeleportOnPVP() {
		return this.blockAnyKindOfTeleportOnPVP;
	}
	public boolean getBlockChorusFruitTeleport() {
		return this.blockChorusFruitTeleport;
	}
	public boolean getBlockEnderPeralTeleport() {
		return this.blockEnderPeralTeleport;
	}
	public boolean getKillWhenLogoutOnPVP() {
		return this.killWhenLogoutOnPVP;
	}
	public boolean getBroadcastPlayerLoggingOnPVP() {
		return this.broadcastPlayerLoggingOnPVP;
	}
	
	protected PVP(ConfigurationSection parentSection, ConfigurationSection defaults) {
		super(parentSection, defaults, "PVP");
		this.checkSection();
	}

	@Override
	protected void checkSection() {
		try {
			timeInPVPMode = Short.parseShort(section.getString("TimeInPVPMode"));
			if(timeInPVPMode<1) throw new IllegalArgumentException();
		} catch(NullPointerException | NumberFormatException e) {
			CheckSectionExpeller.signalConfigurationError(this, "TimeInPVPMode");
			timeInPVPMode = Short.parseShort(defaults.getString("TimeInPVPMode"));
		} catch(IllegalArgumentException e) {
			CheckSectionExpeller.signalConfigurationError(this, "TimeInPVPMode", "TimeInPVPMode - value cannot be lower than 1");
			timeInPVPMode = Short.parseShort(defaults.getString("TimeInPVPMode"));
		}
		try {
			this.turnOffFlyOnPVP = Boolean.parseBoolean(section.getString("TurnOffFlyOnPVP"));
		} catch(NullPointerException e) {
			CheckSectionExpeller.signalConfigurationError(this, "TurnOffFlyOnPVP");
			this.turnOffFlyOnPVP = Boolean.parseBoolean(defaults.getString("TurnOffFlyOnPVP"));
		}
		try {
			this.disableInvisibilityOnPVP = Boolean.parseBoolean(section.getString("DisableInvisibilityOnPVP"));
		} catch(NullPointerException e) {
			CheckSectionExpeller.signalConfigurationError(this, "DisableInvisibilityOnPVP");
			this.disableInvisibilityOnPVP = Boolean.parseBoolean(defaults.getString("DisableInvisibilityOnPVP"));
		}
		try {
			this.blockAllCommandsOnPVP = Boolean.parseBoolean(section.getString("BlockAllCommandsOnPVP"));
		} catch(NullPointerException e) {
			CheckSectionExpeller.signalConfigurationError(this, "BlockAllCommandsOnPVP");
			this.blockAllCommandsOnPVP = Boolean.parseBoolean(defaults.getString("BlockAllCommandsOnPVP"));
		}
		try {
			this.blockAnyKindOfTeleportOnPVP = Boolean.parseBoolean(section.getString("BlockAnyKindOfTeleportOnPVP"));
		} catch(NullPointerException e) {
			CheckSectionExpeller.signalConfigurationError(this, "BlockAnyKindOfTeleportOnPVP");
			this.blockAnyKindOfTeleportOnPVP = Boolean.parseBoolean(defaults.getString("BlockAnyKindOfTeleportOnPVP"));
		}
		try {
			this.blockChorusFruitTeleport = Boolean.parseBoolean(section.getString("BlockChorusFruitTeleport"));
		} catch(NullPointerException e) {
			CheckSectionExpeller.signalConfigurationError(this, "BlockChorusFruitTeleport");
			this.blockChorusFruitTeleport = Boolean.parseBoolean(defaults.getString("BlockChorusFruitTeleport"));
		}
		try {
			this.blockEnderPeralTeleport = Boolean.parseBoolean(section.getString("BlockEnderPeralTeleport"));
		} catch(NullPointerException e) {
			CheckSectionExpeller.signalConfigurationError(this, "BlockEnderPeralTeleport");
			this.blockEnderPeralTeleport = Boolean.parseBoolean(defaults.getString("BlockEnderPeralTeleport"));
		}
		try {
			this.killWhenLogoutOnPVP = Boolean.parseBoolean(section.getString("KillWhenLogoutOnPVP"));
		} catch(NullPointerException e) {
			CheckSectionExpeller.signalConfigurationError(this, "KillWhenLogoutOnPVP");
			this.killWhenLogoutOnPVP = Boolean.parseBoolean(defaults.getString("KillWhenLogoutOnPVP"));
		}
		try {
			this.broadcastPlayerLoggingOnPVP = Boolean.parseBoolean(section.getString("BroadcastPlayerLoggingOnPVP"));
		} catch(NullPointerException e) {
			CheckSectionExpeller.signalConfigurationError(this, "BroadcastPlayerLoggingOnPVP");
			this.broadcastPlayerLoggingOnPVP = Boolean.parseBoolean(defaults.getString("BroadcastPlayerLoggingOnPVP"));
		}
		
	}

}
