package com.gmail.nowyarek.pvpcontrol.configs.config;

import org.bukkit.configuration.file.FileConfiguration;

import com.gmail.nowyarek.pvpcontrol.basic.FileManager;


public class Config {
	private FileManager filemanager;
	@SuppressWarnings("unused")
	private FileConfiguration config;
	
	public Config(FileManager filemanager){
		this.filemanager = filemanager;
		this.config = filemanager.getConfig();
		initializeSections();
	}
	public void reload(boolean reloadFileManagerFile){
		if(reloadFileManagerFile){
			filemanager.reloadConfig();
		}
		this.config = filemanager.getConfig();
		initializeSections();
	}
	
	private void initializeSections(){
		
	}
}
