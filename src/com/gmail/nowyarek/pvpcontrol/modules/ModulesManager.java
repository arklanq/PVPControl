package com.gmail.nowyarek.pvpcontrol.modules;

import java.util.ArrayList;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import com.gmail.nowyarek.pvpcontrol.PVPCore;
import com.gmail.nowyarek.pvpcontrol.configs.ConfigsAccess;
import com.gmail.nowyarek.pvpcontrol.exceptions.ModuleException;
import com.gmail.nowyarek.pvpcontrol.exceptions.PVPCriticalException;

public class ModulesManager {
	private ConfigsAccess configsAccess;
	private ModulesInstancesGetters modulesInstancesGetters;
	private ArrayList<Module> modules = new ArrayList<Module>();
	

	public ModulesManager(PVPCore core) {
		this.configsAccess = core.getConfigsAccess();
		this.modulesInstancesGetters = new ModulesInstancesGetters(core);
	}
	
	public void initializeModules() throws PVPCriticalException {
		//prepare core modules
		for(ModuleType type : ModuleType.values()) {
			try {
				Module module = modulesInstancesGetters.getNewInstanceOfModule(type);
				if(module.shouldBeEnabled()) {
					this.enableModule(module);
					modules.add(module);
				}
			} catch(Exception e) {
				e.printStackTrace();
				if(type.getValidityOfModule() == ValidityOfModule.ESSENTIAL) {
					throw new PVPCriticalException("Highly required module not enabled");
				}
			}
		}
	}
	
	public void deinitalizeModules() {
		for(Module module : modules) {
			try {
				this.disableModule(module);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		modules = new ArrayList<Module>();
	}
	
	private void enableModule(Module module) throws ModuleException {
		try {
			module.onEnable();
		} catch (Exception e) {
			disableModule(module);
			throw new ModuleException(module, e);
		}
	}
	
	private void disableModule(Module module) throws ModuleException {
		try {
			module.onDisable();
		} catch(ModuleException e) {
			if(module instanceof Listener) {
				HandlerList.unregisterAll((Listener) module);
			}
			throw new ModuleException(module, e);
		}
	}
	
	public void reloadAll(boolean reloadConfigsAccess) throws PVPCriticalException {
		if(reloadConfigsAccess) this.configsAccess.reload();
		this.deinitalizeModules();
		this.initializeModules();
	}
	
}
