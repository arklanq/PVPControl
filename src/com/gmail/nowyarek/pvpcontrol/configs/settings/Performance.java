package com.gmail.nowyarek.pvpcontrol.configs.settings;

import org.bukkit.configuration.ConfigurationSection;

import com.gmail.nowyarek.pvpcontrol.configs.CheckSectionExpeller;

public class Performance extends SettingsSubSection {
	private short combatModeCheckSpeed;

	protected Performance(ConfigurationSection parentSection, ConfigurationSection defaults) {
		super(parentSection, defaults, "Performance");
		this.checkSection();
	}
	
	public short getCombatModeCheckSpeed() {
		return this.combatModeCheckSpeed;
	}

	@Override
	protected void checkSection() {
		try {
			combatModeCheckSpeed = Short.parseShort(section.getString("CombatModeCheckSpeed"));
			if(combatModeCheckSpeed<1 || combatModeCheckSpeed>20) throw new IllegalArgumentException();
		} catch(NullPointerException | NumberFormatException e) {
			CheckSectionExpeller.signalConfigurationError(this, "CombatModeCheckSpeed");
			combatModeCheckSpeed = Short.parseShort(defaults.getString("CombatModeCheckSpeed"));
		} catch(IllegalArgumentException e) {
			CheckSectionExpeller.signalConfigurationError(this, "CombatModeCheckSpeed", "combatModeCheckSpeed safe values are 1-20 only!");
			combatModeCheckSpeed = Short.parseShort(defaults.getString("CombatModeCheckSpeed"));
		}
	}

}
