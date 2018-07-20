package com.gmail.nowyarek.pvpcontrol.configs.settings;

import org.bukkit.configuration.ConfigurationSection;

import com.gmail.nowyarek.pvpcontrol.configs.CheckSectionExpeller;

public class Integration extends SettingsSubSection {

	protected Integration(ConfigurationSection parentSection, ConfigurationSection defaults) {
		super(parentSection, defaults, "Hooks");
		checkSection();
	}
	
	// COMPONENTS
	private boolean disableEssentialsGodModeOnHit;

	@Override
	protected void checkSection() {
		try {
			disableEssentialsGodModeOnHit = Boolean.parseBoolean(section.getString("disableEssentialsGodModeOnHit"));
		} catch(Exception e) {
			CheckSectionExpeller.signalConfigurationError(this, "disableEssentialsGodModeOnHit");
			disableEssentialsGodModeOnHit = Boolean.parseBoolean(defaults.getString("disableEssentialsGodModeOnHit"));
		}
	}

	public boolean isDisableEssentialsGodModeOnHit() {
		return disableEssentialsGodModeOnHit;
	}
	
	

}
