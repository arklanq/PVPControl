package com.gmail.nowyarek.pvpcontrol.basic;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class FilesDefaultEntries {
	
	enum FileType{
		SETTINGS, CONFIG, MESSAGES_LAYOUT
	}
	
	FilesDefaultEntries(FileConfiguration given_file, String file_name, FileType type){
		FileConfiguration file = given_file;
		FileConfiguration defaults;
		InputStream is = null;
		switch(type){
			case SETTINGS:
			case CONFIG:
				is = getClass().getResourceAsStream("/"+file_name);
				break;
			case MESSAGES_LAYOUT:
				is = getClass().getResourceAsStream("/lang/"+file_name);
				break;
		}
		Msg msg = new Msg();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		defaults = YamlConfiguration.loadConfiguration(br);
		
		if(type.equals(FileType.MESSAGES_LAYOUT)) {
			msg.addPreloadMessageToSayLater(new PreloadedMessage(Text.LANGUAGE_STATEMENTS_LOADED, new Variables("%number%", ""+file.getKeys(false).size()), PreloadedMessagePriority.LATER));
			int missing = defaults.getKeys(false).size() - file.getKeys(false).size();
			if(missing!=0) msg.addPreloadMessageToSayLater(new PreloadedMessage(Text.MISSING_LANGUAGE_STATEMENTS, new Variables("%number%", ""+missing), PreloadedMessagePriority.LATER));
		}
		if(checkFileCompatibility(file, defaults)){
			msg.addPreloadMessageToSayLater(new PreloadedMessage(Text.OLD_CONFIG, new Variables("%file%", file_name), PreloadedMessagePriority.NORMAL));
		}
	}
	
	private boolean checkFileCompatibility(FileConfiguration file, FileConfiguration defaults){
		return checkAllSubdirectories((ConfigurationSection) file, (ConfigurationSection) defaults, false);
	}

	private boolean checkAllSubdirectories(ConfigurationSection file, ConfigurationSection defaults, boolean isold){
		boolean oldconfig = isold;
		for(String key : defaults.getKeys(false)){
			if(!defaults.isConfigurationSection(key)){
				if(defaults.isSet(key)){
					if(!file.isSet(key)){
						file.set(key, defaults.get(key));
						oldconfig = true;
					}
				}
			}else{
				ConfigurationSection file_cs = file.getConfigurationSection(key);
				if(file_cs==null){
					file_cs = file.createSection(key);
					oldconfig = true;
				}
				ConfigurationSection default_cs = defaults.getConfigurationSection(key);
				oldconfig = checkAllSubdirectories(file_cs, default_cs, oldconfig);
			}
		}
		return oldconfig;
	}
	
}
