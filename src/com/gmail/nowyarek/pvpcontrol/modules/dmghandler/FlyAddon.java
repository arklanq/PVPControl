package com.gmail.nowyarek.pvpcontrol.modules.dmghandler;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.gmail.nowyarek.pvpcontrol.configs.ConfigsAccess;

class FlyAddon implements EntityDmgHandlerAddon {
	private boolean shouldBeUsed;

	FlyAddon(ConfigsAccess configsAccess) {
		this.shouldBeUsed = configsAccess.settings.pvp.getTurnOffFlyOnPVP();
	}
	
	@Override
	public boolean shouldBeUsed() {
		return shouldBeUsed;
	}

	@Override
	public void run(EntityDamageByEntityEvent e, Player victim, Player damager) {
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

}
