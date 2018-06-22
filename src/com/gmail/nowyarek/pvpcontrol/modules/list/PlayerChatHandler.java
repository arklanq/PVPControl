package com.gmail.nowyarek.pvpcontrol.modules.list;

import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

import com.gmail.nowyarek.pvpcontrol.basic.Msg;
import com.gmail.nowyarek.pvpcontrol.basic.Text;
import com.gmail.nowyarek.pvpcontrol.configs.ConfigsAccess;
import com.gmail.nowyarek.pvpcontrol.exceptions.ModuleException;
import com.gmail.nowyarek.pvpcontrol.modules.Module;
import com.gmail.nowyarek.pvpcontrol.pvpmode.PVPModeHandler;

public class PlayerChatHandler extends Module implements Listener {
	private Plugin plugin;
	private ConfigsAccess configsAccess;
	private PVPModeHandler pvpModeHandler;
	
	public PlayerChatHandler(Plugin plugin, ConfigsAccess configsAccess, PVPModeHandler pvpModeHandler) {
		this.plugin = plugin;
		this.configsAccess = configsAccess;
		this.pvpModeHandler = pvpModeHandler;
	}
	
	@EventHandler
	public void onPlayerTeleport(PlayerCommandPreprocessEvent e) {
		if(e.getMessage().startsWith("/pvp")) return;
		if(!pvpModeHandler.isPlayerInCombat(e.getPlayer().getUniqueId())) return;
		if(e.getPlayer().hasPermission("pvpc.bypass.commands")) return;
		
		if(configsAccess.settings.pvp.getBlockAllCommandsOnPVP()) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(Msg.warn(Text.COMMANDS_NOT_ALLOWED_DURING_PVP));
			return;
		}
		
		String cmd = e.getMessage().substring(1);
		
		for(String blockedCmd : configsAccess.settings.other.getListOfBlockedCommands()) {
			if(blockedCmd.equalsIgnoreCase(cmd)) {
				e.setCancelled(true);
				e.getPlayer().sendMessage(Msg.warn(Text.COMMANDS_NOT_ALLOWED_DURING_PVP));
				break;
			}
		}
	}

	@Override
	public void onEnable() throws ModuleException {
		try {
			plugin.getServer().getPluginManager().registerEvents(this, plugin);
		} catch(Exception e) {
			throw new ModuleException(e, getName(), "Error while enabling module");
		}
		this.enabled = true;
	}

	@Override
	public void onDisable() throws ModuleException {
		try {
			HandlerList.unregisterAll(this);
		} catch(Exception e) {
			throw new ModuleException(e, getName(), "Error while disabling module");
		}
		this.enabled = false;
	}

	@Override
	public void onReload() throws ModuleException {
		try {
			this.onDisable();
			this.onEnable();
		} catch(Exception e) {
			throw new ModuleException(e, getName(), "Error while reloading module");
		}
	}

	@Override
	public String getName() {
		return this.getClass().getName();
	}

}
