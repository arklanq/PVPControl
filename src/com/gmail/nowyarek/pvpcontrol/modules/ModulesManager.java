package com.gmail.nowyarek.pvpcontrol.modules;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import com.gmail.nowyarek.pvpcontrol.PVPCore;
import com.gmail.nowyarek.pvpcontrol.configs.ConfigsAccess;
import com.gmail.nowyarek.pvpcontrol.exceptions.ModuleException;
import com.gmail.nowyarek.pvpcontrol.exceptions.PVPCriticalException;
import com.gmail.nowyarek.pvpcontrol.exceptions.PVPSoftException;
import com.gmail.nowyarek.pvpcontrol.modules.list.EntityDamageHandler;
import com.gmail.nowyarek.pvpcontrol.modules.list.PlayerChatHandler;
import com.gmail.nowyarek.pvpcontrol.modules.list.PlayerDeathHandler;
import com.gmail.nowyarek.pvpcontrol.modules.list.PlayerLogOutHandler;
import com.gmail.nowyarek.pvpcontrol.modules.list.PlayerTeleportHandler;

public class ModulesManager {
	private ConfigsAccess configsAccess;
	private EntityDamageHandler entityDamageHandler;
	private PlayerTeleportHandler playerTeleportHandler;
	private PlayerLogOutHandler playerLogOutHandler;
	private PlayerChatHandler playerChatHandler;
	private PlayerDeathHandler playerDeathHandler;
	
	PlayerDeathHandler getPlayerDeathHandler() {
		return playerDeathHandler;
	}

	public ModulesManager(PVPCore core) {
		this.configsAccess = core.getConfigsAccess();
		entityDamageHandler = new EntityDamageHandler(
				core.getPlugin(),
				core.getConfigsAccess(),
				core.getPVPModeHandler()
				);
		playerTeleportHandler = new PlayerTeleportHandler(
				core.getPlugin(),
				core.getConfigsAccess(),
				core.getPVPModeHandler()
				);
		playerLogOutHandler = new PlayerLogOutHandler(
				core.getPlugin(),
				core.getConfigsAccess(),
				core.getPVPModeHandler()
				);
		playerChatHandler = new PlayerChatHandler(
				core.getPlugin(),
				core.getConfigsAccess(),
				core.getPVPModeHandler()
				);
		playerDeathHandler = new PlayerDeathHandler(
				core.getPlugin(),
				core.getPVPModeHandler()
				);
	}
	
	public void initializeModules() throws PVPCriticalException {
		this.enableModule(entityDamageHandler);
		if(entityDamageHandler==null || !entityDamageHandler.isEnabled()) {
			throw new PVPCriticalException("Failed to launch EntityDamageHandler module!");
		}
		
		this.enableModule(playerDeathHandler);
		if(playerDeathHandler==null || !playerDeathHandler.isEnabled()) {
			throw new PVPCriticalException("Failed to launch playerDeathHandler module!");
		}
		
		if( configsAccess.settings.pvp.getBlockAnyKindOfTeleportOnPVP()
				|| configsAccess.settings.pvp.getBlockChorusFruitTeleport()
				|| configsAccess.settings.pvp.getBlockEnderPeralTeleport()
				) {
			this.enableModule(playerTeleportHandler);
			if(playerTeleportHandler==null || !playerTeleportHandler.isEnabled()) {
				throw new PVPCriticalException("Failed to launch PlayerTeleportHandler module!");
			}
		}
		if( configsAccess.settings.pvp.getKillWhenLogoutOnPVP()
				|| configsAccess.settings.other.getListOfLogOutCommands().size()!=0) {
			this.enableModule(playerLogOutHandler);
			if(playerLogOutHandler==null || !playerLogOutHandler.isEnabled()) {
				throw new PVPCriticalException("Failed to launch PlayerLogOutHandler module!");
			}
		}
		if( configsAccess.settings.pvp.getBlockAllCommandsOnPVP()
				|| configsAccess.settings.other.getListOfBlockedCommands().size()!=0) {
			this.enableModule(playerChatHandler);
			if(playerChatHandler==null || !playerChatHandler.isEnabled()) {
				throw new PVPCriticalException("Failed to launch PlayerChatHandler module!");
			}
		}
	}
	
	public void deinitalizeModules() {
		try {
			this.disableModule(entityDamageHandler);
			this.disableModule(playerTeleportHandler);
			this.disableModule(playerLogOutHandler);
			this.disableModule(playerChatHandler);
		} catch(Exception e) {
			e.printStackTrace();
			throw new PVPSoftException();
		}
	}
	
	public void reload(boolean reloadConfigsAccess) throws PVPCriticalException {
		if(reloadConfigsAccess) this.configsAccess.reload();
		this.deinitalizeModules();
		this.initializeModules();
	}
	
	private boolean enableModule(Module module) {
		try {
			module.onEnable();
		} catch (Exception e) {
			disableModule(module);
			return false;
		}
		return true;
	}
	
	private void disableModule(Module module) {
		try {
			module.onDisable();
		} catch(ModuleException e) {
			if(module instanceof Listener) {
				HandlerList.unregisterAll((Listener) module);
			}
			module = null;
		}
	}
	
}
