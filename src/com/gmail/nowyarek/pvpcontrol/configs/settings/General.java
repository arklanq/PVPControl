package com.gmail.nowyarek.pvpcontrol.configs.settings;

import org.bukkit.configuration.ConfigurationSection;


public class General extends SettingsSubSection {
	
	// COMPONENTS
	private String lang;
	private int maxLogSize;

	protected General(ConfigurationSection parentSection, ConfigurationSection defaults) {
		super(parentSection, defaults, "General");
		this.checkSection();
	}
	
	public String getLanguage(){
		return lang;
	}
	
	public int getMaxLogSize() {
		return this.maxLogSize;
	}

	@Override
	protected void checkSection() {
		//already checked in FileManager
		lang = section.getString("Language");
		maxLogSize = Integer.parseInt(section.getString("MaxLogSize"));
	}

}
