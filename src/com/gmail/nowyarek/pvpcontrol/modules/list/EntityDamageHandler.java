package com.gmail.nowyarek.pvpcontrol.modules.list;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.gmail.nowyarek.pvpcontrol.basic.Msg;
import com.gmail.nowyarek.pvpcontrol.basic.Text;
import com.gmail.nowyarek.pvpcontrol.configs.ConfigsAccess;
import com.gmail.nowyarek.pvpcontrol.exceptions.ModuleException;
import com.gmail.nowyarek.pvpcontrol.integration.DependencyType;
import com.gmail.nowyarek.pvpcontrol.integration.Integration;
import com.gmail.nowyarek.pvpcontrol.modules.Module;
import com.gmail.nowyarek.pvpcontrol.pvpmode.PVPModeHandler;

public class EntityDamageHandler extends Module implements Listener {
	private Plugin plugin;
	private ConfigsAccess configsAccess;
	private PVPModeHandler pvpModeHandler;
	private final Essentials essentials;
	
	public EntityDamageHandler(Plugin plugin, ConfigsAccess configsAccess, PVPModeHandler pvpModeHandler, Integration integration) {
		this.plugin = plugin;
		this.configsAccess = configsAccess;
		this.pvpModeHandler = pvpModeHandler;
		if(integration.check(DependencyType.ESSENTIALSX)) {
			this.essentials = integration.getEssentials();
		} else
			this.essentials = null;
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		
		if(!(e.getEntity() instanceof Player)) return;
		if(e.getEntity().hasMetadata("npc") || e.getDamager().hasMetadata("npc")) return;
		Player victim = (Player) e.getEntity();
		Player damager;
		if(e.getDamager() instanceof Player) {
			damager = (Player) e.getDamager();
		}else {
			if(e.getDamager() instanceof Projectile) {
				if( ((Projectile) e.getDamager()).getShooter() instanceof Player ) {
					damager = (Player)((Projectile) e.getDamager()).getShooter();
				} else
					return;
			} else
				return;
		}
		
		if(e.getEntity().getUniqueId().compareTo(e.getDamager().getUniqueId())==0) return;
		
		if(victim.hasMetadata("pvpmode.admin")) {
			e.setCancelled(true);
			return;
		}
		if(damager.hasMetadata("pvpmode.admin")) {
			e.setCancelled(true);
			return;
		}
		
		if(essentials!=null && configsAccess.settings.integrationSection.isDisableEssentialsGodModeOnHit()) {
			User user = essentials.getUser(victim.getUniqueId());
			if(user.isGodModeEnabled()) {
				user.setGodModeEnabled(false);
				e.setCancelled(false);
				victim.sendMessage(Msg.info(Text.GOD_MODE_HAS_BEEN_DISABLED));
			}
		}
		
		if(e.isCancelled())
			return;
		
		if(configsAccess.settings.pvp.getTurnOffFlyOnPVP()) {
			if(!victim.hasPermission("pvpc.bypass.fly")) {
				if(victim.isFlying()) {
					victim.setFlying(false);
				}
				victim.setAllowFlight(false);
			}
			if(!damager.hasPermission("pvpc.bypass.fly"))  {
				if(damager.isFlying()) {
					damager.setFlying(false);
				}
				damager.setAllowFlight(false);
			}
		}
		
		if(configsAccess.settings.pvp.getDisableInvisibilityOnPVP()) {
			if(victim.hasPotionEffect(PotionEffectType.INVISIBILITY) 
					&& !isVanished(victim) 
					&& !victim.hasPermission("pvpc.bypass.invisibility")) {
				victim.removePotionEffect(PotionEffectType.INVISIBILITY);
			}
			if(damager.hasPotionEffect(PotionEffectType.INVISIBILITY) 
					&& !isVanished(damager) 
					&& !victim.hasPermission("pvpc.bypass.invisibility")) {
				damager.removePotionEffect(PotionEffectType.INVISIBILITY);
			}
		}
		
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
	
	private boolean isVanished(Player player) {
		for (MetadataValue meta : player.getMetadata("vanished")) {
            if (meta.asBoolean()) return true;
        }
		return false;
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
