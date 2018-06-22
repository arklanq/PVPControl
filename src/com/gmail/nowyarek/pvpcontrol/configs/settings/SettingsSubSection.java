package com.gmail.nowyarek.pvpcontrol.configs.settings;

import org.bukkit.configuration.ConfigurationSection;

public abstract class SettingsSubSection {
	protected ConfigurationSection section, defaults;
	protected String sectionPathName;
	
	protected SettingsSubSection(ConfigurationSection parentSection, ConfigurationSection defaults, String section_name){
		this.sectionPathName = section_name;
		this.section = parentSection.getConfigurationSection(section_name);
		this.defaults = defaults.getConfigurationSection(section_name);
	}
	
	protected abstract void checkSection();
	protected void initializeSubSections() { }
	
	public String getPath() {
		String path = "";
		ConfigurationSection temp = this.section;
		for(;temp!=null;) {
			path += "."+temp.getName();
			temp = temp.getParent();
		}
		char x = path.charAt(0);
		if(x == '.') {
			path.substring(1);
		}
		return path;
	}
	
}
