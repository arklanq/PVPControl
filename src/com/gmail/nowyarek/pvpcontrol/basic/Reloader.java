package com.gmail.nowyarek.pvpcontrol.basic;

import com.gmail.nowyarek.pvpcontrol.PVPCore;
import com.gmail.nowyarek.pvpcontrol.commands.pvpc.HelpPrinter;
import com.gmail.nowyarek.pvpcontrol.exceptions.PVPCriticalException;

public class Reloader {
	private PVPCore pvpCore;
	private HelpPrinter helpPrinter;
	
	public Reloader(PVPCore core){
		this.pvpCore = core;
	}
	
	public void addHelpPrinter(HelpPrinter helpPrinter) {
		this.helpPrinter = helpPrinter;
	}
	
	public void reloadWholeConfiguration(){
		reloadSettings();
		reloadConfig();
		reloadTranslations();
		reloadHelp();
		reloadPVPModeHandler();
		reloadModulesManager();
	}
	
	public void reloadTranslations(){
		Msg msg = new Msg();
		msg.reload(true);
		reloadHelp();
	}
	public void reloadSettings(){
		pvpCore.getConfigsAccess().settings.reload(true);
	}
	public void reloadConfig(){
		pvpCore.getConfigsAccess().config.reload(true);
	}
	void reloadHelp(){
		helpPrinter.reload();
		
	}
	void reloadPVPModeHandler() {
		pvpCore.getPVPModeHandler().reload(false);
	}
	void reloadModulesManager() {
		try {
			pvpCore.getModulesManager().reloadAll(false);
		} catch (PVPCriticalException e) {
			e.printStackTrace();
			pvpCore.getPlugin().getServer().getPluginManager().disablePlugin(pvpCore.getPlugin());
		}
	}
}
