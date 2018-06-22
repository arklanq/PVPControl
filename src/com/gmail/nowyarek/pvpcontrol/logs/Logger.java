package com.gmail.nowyarek.pvpcontrol.logs;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;


interface Logger {
	
	String getLogName();
	File getFile();
	FileConfiguration getFileConfiguration();
	int getLastLine();
	
	public void log(String msg);

	void initialize();
	void deinitialize();
	
}
