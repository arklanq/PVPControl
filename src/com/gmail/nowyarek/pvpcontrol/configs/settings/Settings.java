package com.gmail.nowyarek.pvpcontrol.configs.settings;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.gmail.nowyarek.pvpcontrol.basic.FileManager;

public class Settings {
	private FileManager filemanager;
	private FileConfiguration settings;
	
	//SUB-SECTIONS
	public General general;
	public Other other;
	public PVP pvp;
	public Performance performance;
	
	public Settings(FileManager filemanager){
		this.filemanager = filemanager;
		this.settings = filemanager.getSettings();
		initializeSections();
	}
	public void reload(boolean reloadFileManagerFile){
		if(reloadFileManagerFile){
			filemanager.reloadSettings();
		}
		this.settings = filemanager.getSettings();
		initializeSections();
	}
	
	private void initializeSections(){
		InputStream is = getClass().getResourceAsStream("/settings.yml");
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		FileConfiguration defaults = YamlConfiguration.loadConfiguration(br);
		
		general = new General(settings, defaults);
		other = new Other(settings, defaults);
		pvp = new PVP(settings, defaults);
		performance = new Performance(settings, defaults);
	}
}



