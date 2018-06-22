package com.gmail.nowyarek.pvpcontrol;

import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.nowyarek.pvpcontrol.basic.FileManager;
import com.gmail.nowyarek.pvpcontrol.basic.Msg;
import com.gmail.nowyarek.pvpcontrol.basic.Text;
import com.gmail.nowyarek.pvpcontrol.configs.ConfigsAccess;
import com.gmail.nowyarek.pvpcontrol.exceptions.PVPCriticalException;


public class PVPControl extends JavaPlugin {
	private PVPCore core;
	public static long mainThreadID;

	@Override
	public void onEnable() {
		mainThreadID = Thread.currentThread().getId();
		FileManager fileManager = new FileManager(this);
		try {
			fileManager.createFiles();
			fileManager.initalizeLogger();
		} catch (PVPCriticalException e) {
			e.printStackTrace();
			this.getServer().getPluginManager().disablePlugin(this);
		}
		
		Msg msg = new Msg();
		msg.InitalizeMsg(fileManager);
		
		ConfigsAccess configsAccess = new ConfigsAccess(fileManager);
		
		this.core = new PVPCore(this, fileManager, configsAccess);
		try {
			core.onEnable();
		} catch (PVPCriticalException e) {
			e.printStackTrace();
		}
		
		Msg.serverAnnoucement(Text.PLUGIN_ENABLED);
	}
	
	@Override
	public void onDisable() {
		if(core!=null) {
			core.onDisable();
		}
		Msg.consoleLog(Text.PLUGIN_DISABLED);
	}
	
	public PVPCore getPVPCore() {
		return this.core;
	}
	
}
