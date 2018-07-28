package com.gmail.nowyarek.pvpcontrol.modules.other;

import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import com.gmail.nowyarek.pvpcontrol.basic.Msg;
import com.gmail.nowyarek.pvpcontrol.basic.Text;
import com.gmail.nowyarek.pvpcontrol.basic.Variables;
import com.gmail.nowyarek.pvpcontrol.configs.ConfigsAccess;
import com.gmail.nowyarek.pvpcontrol.exceptions.ModuleException;
import com.gmail.nowyarek.pvpcontrol.modules.Module;
import com.gmail.nowyarek.pvpcontrol.modules.ModuleType;
import com.gmail.nowyarek.pvpcontrol.pvpmode.PVPModeHandler;

public class PlayerLogOutHandler extends Module implements Listener {
	private Plugin plugin;
	private ConfigsAccess configsAccess;
	private PVPModeHandler pvpModeHandler;
	
	public PlayerLogOutHandler(Plugin plugin, ConfigsAccess configsAccess, PVPModeHandler pvpModeHandler) {
		this.plugin = plugin;
		this.configsAccess = configsAccess;
		this.pvpModeHandler = pvpModeHandler;
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		if(!pvpModeHandler.isPlayerInCombat(e.getPlayer().getUniqueId())) return;
		if(configsAccess.settings.pvp.getKillWhenLogoutOnPVP()) {
			e.getPlayer().setHealth(0);
			//drop items in a special way?
			if(configsAccess.settings.pvp.getBroadcastPlayerLoggingOnPVP()) {
				//say
				plugin.getServer().broadcastMessage(Msg.info(Text.PLAYER_LOG_OUT_DURING_PVP_SERVER_BROADCAST, new Variables("%player%", e.getPlayer().getName())));
			}
		}
		for(String msg : configsAccess.settings.other.getListOfLogOutCommands()) {
			if(msg.contains("%player%")) {
				msg = msg.replaceAll("%player%", e.getPlayer().getName());
			}
			plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), msg);
		}
		pvpModeHandler.getAllPlayerInPVPWithTimings().remove(e.getPlayer().getUniqueId());
	}

	@Override
	public void onEnable() throws ModuleException {
		try {
			plugin.getServer().getPluginManager().registerEvents(this, plugin);
		} catch(Exception e) {
			throw new ModuleException(this, e, "Error while enabling module");
		}
		this.enabled = true;
	}

	@Override
	public void onDisable() throws ModuleException {
		try {
			HandlerList.unregisterAll(this);
		} catch(Exception e) {
			throw new ModuleException(this, e, "Error while disabling module");
		}
		this.enabled = false;
	}

	@Override
	public ModuleType getType() {
		return ModuleType.PLAYER_LOG_OUT_HANDLER;
	}

	@Override
	public boolean shouldBeEnabled() {
		return true;
	}

}
