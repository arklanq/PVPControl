package com.gmail.nowyarek.pvpcontrol;

import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.nowyarek.pvpcontrol.basic.FileManager;
import com.gmail.nowyarek.pvpcontrol.basic.Reloader;
import com.gmail.nowyarek.pvpcontrol.commands.pvp.PVPCommand;
import com.gmail.nowyarek.pvpcontrol.commands.pvpc.PvpcCommand;
import com.gmail.nowyarek.pvpcontrol.configs.ConfigsAccess;
import com.gmail.nowyarek.pvpcontrol.exceptions.PVPCriticalException;
import com.gmail.nowyarek.pvpcontrol.exceptions.PVPSoftException;
import com.gmail.nowyarek.pvpcontrol.integration.Integration;
import com.gmail.nowyarek.pvpcontrol.modules.ModulesManager;
import com.gmail.nowyarek.pvpcontrol.pvpmode.PVPModeHandler;

public class PVPCore {
	private JavaPlugin plugin;
	private FileManager fileManager;
	private ConfigsAccess configsAccess;
	private PVPModeHandler pvpModeHandler;
	private ModulesManager modulesManager;
	private PvpcCommand pvpcCommand;
	private Integration integration;
	private Reloader reloader;
	
	PVPCore(JavaPlugin plugin, FileManager fileManager, ConfigsAccess configsAccess){
		this.plugin = plugin;
		this.fileManager = fileManager;
		this.configsAccess = configsAccess;
	}
	
	void onEnable() throws PVPCriticalException {
		try {
			pvpModeHandler = new PVPModeHandler(plugin, configsAccess);
		} catch(Exception e) {
			throw new PVPCriticalException("PVPModeHandler, exception occured");
		}
		integration = new Integration(plugin);
		integration.checkDependency();
		modulesManager = new ModulesManager(this);
		modulesManager.initializeModules();
		reloader = new Reloader(this);
		pvpcCommand = new PvpcCommand(this);
		reloader.addHelpPrinter(pvpcCommand.getHelpPrinter());
		new PVPCommand(getPlugin(), this.pvpModeHandler);
	}
	
	void onDisable() throws PVPSoftException {
		pvpModeHandler.stop();
		modulesManager.deinitalizeModules();
	}
	
	public JavaPlugin getPlugin() {
		return this.plugin;
	}
	public ConfigsAccess getConfigsAccess() {
		return configsAccess;
	}
	public FileManager getFileManager() {
		return this.fileManager;
	}
	public PVPModeHandler getPVPModeHandler() {
		return this.pvpModeHandler;
	}
	public ModulesManager getModulesManager() {
		return this.modulesManager;
	}
	public Reloader getReloader() {
		return this.reloader;
	}

	public Integration getIntegration() {
		return integration;
	}
	
}
