package com.gmail.nowyarek.pvpcontrol.basic;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.gmail.nowyarek.pvpcontrol.basic.FilesDefaultEntries.FileType;
import com.gmail.nowyarek.pvpcontrol.exceptions.LogFileHandleException;
import com.gmail.nowyarek.pvpcontrol.exceptions.PVPCriticalException;
import com.gmail.nowyarek.pvpcontrol.logs.LogsManager;

public class FileManager{
	private Plugin plugin;
	private File configf, msgs_layoutf, settingsf;
	private FileConfiguration config, msgs_layout, settings;
	private File dataFolder, logsDir;
	private LogsManager logsManager;
	
	public FileManager(Plugin plugin){
		this.plugin = plugin;
		this.dataFolder = plugin.getDataFolder();
	}
	//========================================PREPARING FILES
	public void createFiles() throws PVPCriticalException {
		if(!dataFolder.exists()) dataFolder.mkdirs();
		logsDir = new File(dataFolder, "logs");
		if(!logsDir.exists()) logsDir.mkdirs();
		
		settingsf = new File(dataFolder, "settings.yml");
		configf = new File(dataFolder, "config.yml");

		if (!settingsf.exists()) {
			plugin.saveResource("settings.yml", false);
		}
		if (!configf.exists()) {
			plugin.saveResource("config.yml", false);
		}

		settings = YamlConfiguration.loadConfiguration(settingsf);
		new FilesDefaultEntries(settings, settingsf.getName(), FileType.SETTINGS);
		config = YamlConfiguration.loadConfiguration(configf);
		new FilesDefaultEntries(config, configf.getName(), FileType.CONFIG);
		
		createLangFile(plugin);
	}
	
	private void createLangFile(Plugin plugin) throws PVPCriticalException{
		if(settings.getString("General.Language")==null){
			settings.set("General.Language", "PL");
		}
		File langFolder = new File(dataFolder, "lang");
		langFolder.mkdir();
		msgs_layoutf = new File(langFolder, "messages_"+this.settings.getString("General.Language").toLowerCase()+".yml");
		if (!msgs_layoutf.exists()) {
			try{
				
				plugin.saveResource("lang/messages_"+this.settings.getString("General.Language").toLowerCase()+".yml", false);
				
			} catch(NullPointerException e){
				Msg msg = new Msg();
				msg.addPreloadMessageToSayLater(new PreloadedMessage(Text.LANGUAGE_NOT_SUPPORTED, new Variables("%lang%", this.settings.getString("General.Language")), PreloadedMessagePriority.EARLY));
				getSettings().set("General.Language", "EN");
				
				try {
					msgs_layoutf.createNewFile();
					msgs_layout = YamlConfiguration.loadConfiguration(
							new BufferedReader(
									new InputStreamReader(
											getClass().getResourceAsStream(
													"/lang/messages_"
														+ this.settings.getString("General.Language").toLowerCase()
														+ ".yml"
													)
											)
									)
							);
					msgs_layout.save(msgs_layoutf);
				} catch (IOException e2) {
					e.printStackTrace();
					throw new PVPCriticalException();
				}
				
			}
		}
		msgs_layout = YamlConfiguration.loadConfiguration(msgs_layoutf);
		new FilesDefaultEntries(msgs_layout, msgs_layoutf.getName(), FileType.MESSAGES_LAYOUT);
	}
	
	public void initalizeLogger() {
		int maxLogSize;
		try {
			if(!settings.isSet("General.MaxLogSize")) {
				settings.set("General.MaxLogSize", "1000");
				/*Msg msg = new Msg();
				msg.addPreloadMessageToSayLater(new PreloadedMessage());*/
			}
			maxLogSize = Integer.parseInt(settings.getString("General.MaxLogSize"));
		} catch(NumberFormatException | NullPointerException e) {
			maxLogSize = 1000;
		}
		logsManager = new LogsManager(plugin, this, maxLogSize);
	}
	
	public LogsManager getLogsManager() {
		return this.logsManager;
	}
	
	public File getCurrentLogFile(String name) {
		File dir = new File(logsDir, name);
		if(!dir.exists()) dir.mkdirs();
		return this.getLogFile(name, this.getLastLogFileNumber(dir, name));
	}
	
	public File getNextLogFile(String name) {
		File dir = new File(logsDir, name);
		if(!dir.exists()) dir.mkdirs();
		return this.getLogFile(name, this.getLastLogFileNumber(dir, name)+1);
	}
	
	public File getLogFile(String name, int index) {
		try {
			File dir = new File(logsDir, name);
			if(!dir.exists()) dir.mkdirs();
			File log = new File(dir, name + index +".yml");
			if(!log.exists()) log.createNewFile();
			return log;
		} catch(Exception e) {
			throw new LogFileHandleException(name);
		}
	}
	
	private int getLastLogFileNumber(File dir, String fileName) {
		try {
			String[] files = dir.list();
			int page = 1;
			for(int i=0; i<files.length; i++) {
				int x = Integer.parseInt(files[i].toString().replaceAll(".yml", "").substring(fileName.length()));
				if(x>page) page = x;
			}
			return page;
		} catch(Exception e) {
			e.printStackTrace();
			throw new LogFileHandleException(fileName);
		}
		
	}
	
	//========================================GET
	public FileConfiguration getSettings(){
		return this.settings;
	}
	public FileConfiguration getConfig(){
		return this.config;
	}
	public FileConfiguration getMsgsLayout(){
		return this.msgs_layout;
	}
	
	//========================================SAVE
	public void saveSettings(){
		try {
			this.settings.save(settingsf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void saveConfig(){
		try {
			this.config.save(configf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void saveMsgsLauout(){
		try {
			this.msgs_layout.save(msgs_layoutf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//========================================RELOAD
	public void reloadSettings(){
		settings = YamlConfiguration.loadConfiguration(settingsf);
		new FilesDefaultEntries(settings, settingsf.getName(), FileType.SETTINGS);
	}
	public void reloadConfig(){
		config = YamlConfiguration.loadConfiguration(configf);
		new FilesDefaultEntries(config, configf.getName(), FileType.CONFIG);
	}
	public void reloadMsgsLayout(){
		try {
			createLangFile(plugin);
		} catch (PVPCriticalException e) {
			e.printStackTrace();
		}	// CHANGE LANGUAGE?
	}
}






