package com.gmail.nowyarek.pvpcontrol.modules.list;

import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;

import com.gmail.nowyarek.pvpcontrol.exceptions.ModuleException;
import com.gmail.nowyarek.pvpcontrol.modules.Module;
import com.gmail.nowyarek.pvpcontrol.pvpmode.PVPModeHandler;

public class PlayerDeathHandler extends Module implements Listener {
	private Plugin plugin;
	private PVPModeHandler pvpHandler;
	
	public PlayerDeathHandler(Plugin plugin, PVPModeHandler pvpHandler) {
		this.plugin = plugin;
		this.pvpHandler = pvpHandler;
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		if(!pvpHandler.isPlayerInCombat(e.getEntity().getUniqueId())) return;
		pvpHandler.turnOffCombatModeForPlayer(e.getEntity().getUniqueId());
	}

	@Override
	public void onEnable() throws ModuleException {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.enabled = true;
	}

	@Override
	public void onDisable() throws ModuleException {
		HandlerList.unregisterAll(this);
		this.enabled = false;
	}

	@Override
	public void onReload() throws ModuleException {
		this.onDisable();
		this.onEnable();
	}

	@Override
	public String getName() {
		return this.getClass().getName();
	}

}
