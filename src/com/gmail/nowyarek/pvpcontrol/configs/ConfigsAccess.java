package com.gmail.nowyarek.pvpcontrol.configs;

import com.gmail.nowyarek.pvpcontrol.basic.FileManager;
import com.gmail.nowyarek.pvpcontrol.configs.config.Config;
import com.gmail.nowyarek.pvpcontrol.configs.settings.Settings;

public class ConfigsAccess {
	
	public Settings settings;
	public Config config;
	public FileManager filemanager;
	
	public ConfigsAccess(FileManager filemanager){
		this.filemanager = filemanager;
		this.settings = new Settings(filemanager);
		this.config = new Config(filemanager);
	}
	
	public void reload() {
		this.filemanager.reloadSettings();
		this.settings = new Settings(filemanager);
		this.filemanager.reloadConfig();
		this.config = new Config(filemanager);
	}
}
