package com.gmail.nowyarek.pvpcontrol.modules.dmghandler;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;

import com.gmail.nowyarek.pvpcontrol.PVPCore;
import com.gmail.nowyarek.pvpcontrol.exceptions.ModuleException;
import com.gmail.nowyarek.pvpcontrol.modules.Module;
import com.gmail.nowyarek.pvpcontrol.modules.ModuleType;
import com.gmail.nowyarek.pvpcontrol.pvpmode.PVPModeHandler;

public class EntityDamageHandler extends Module implements Listener {
	private Plugin plugin;
	private PVPModeHandler pvpModeHandler;
	private EntityDmgHandlerAddonsManager addonsManager;
	private final boolean ignoreCancelled;
	
	public EntityDamageHandler(PVPCore core) {
		this.plugin = core.getPlugin();
		this.pvpModeHandler = core.getPVPModeHandler();
		this.addonsManager = new EntityDmgHandlerAddonsManager(core);
		this.ignoreCancelled = addonsManager.getCancellationSensitiveAddons().size()==0;
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		if(e.isCancelled() && ignoreCancelled) return;
		if(!(e.getEntity() instanceof Player)) return;
		if(e.getEntity().hasMetadata("npc") || e.getDamager().hasMetadata("npc")) return;
		Player victim = (Player) e.getEntity();
		Player damager;
		if(e.getDamager() instanceof Player) {
			damager = (Player) e.getDamager();
		} else {
			if(e.getDamager() instanceof Projectile) {
				if( ((Projectile) e.getDamager()).getShooter() instanceof Player ) {
					damager = (Player)((Projectile) e.getDamager()).getShooter();
				} else
					return;
			} else
				return;
		}
		
		if(victim.getUniqueId().compareTo(damager.getUniqueId())==0) return;
		
		if(victim.hasMetadata("pvpmode.admin")) {
			e.setCancelled(true);
			return;
		}
		if(damager.hasMetadata("pvpmode.admin")) {
			e.setCancelled(true);
			return;
		}
		
		//run all "before-cancel" addons
		if(!ignoreCancelled) {
			for(EntityDmgHandlerAddon addon : addonsManager.getCancellationSensitiveAddons()) {
				addon.run(e, victim, damager);
			}
		}

		if(e.isCancelled()) return;
		
		//run all "after-cancel" addons
		for(EntityDmgHandlerAddon addon : addonsManager.getIgnoreCancellationAddons()) {
			addon.run(e, victim, damager);
		}
		
		if(e.isCancelled()) return;
		
		if(pvpModeHandler.getPlayerPVPStartTime(victim.getUniqueId())!=0) {
			pvpModeHandler.updatePlayerPVPStartTime(victim.getUniqueId());
		}else {
			pvpModeHandler.turnOnCombatModeForPlayer(victim);
		}
		
		if(pvpModeHandler.getPlayerPVPStartTime(damager.getUniqueId())!=0) {
			pvpModeHandler.updatePlayerPVPStartTime(damager.getUniqueId());
		}else {
			pvpModeHandler.turnOnCombatModeForPlayer(damager);
		}
		
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
	public boolean shouldBeEnabled() {
		return true;
	}

	@Override
	public ModuleType getType() {
		return ModuleType.ENTITY_DAMAGE_HANDLER;
	}

}
