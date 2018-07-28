package com.gmail.nowyarek.pvpcontrol.modules.dmghandler;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffectType;

import com.gmail.nowyarek.pvpcontrol.configs.ConfigsAccess;

class InvisibilityAddon implements EntityDmgHandlerAddon {
	private boolean shouldBeUsed;

	InvisibilityAddon(ConfigsAccess configsAccess) {
		this.shouldBeUsed = configsAccess.settings.pvp.getDisableInvisibilityOnPVP();
	}
	
	@Override
	public boolean shouldBeUsed() {
		return shouldBeUsed;
	}

	@Override
	public void run(EntityDamageByEntityEvent e, Player victim, Player damager) {
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
	
	private boolean isVanished(Player player) {
		for (MetadataValue meta : player.getMetadata("vanished")) {
            if (meta.asBoolean()) return true;
        }
		return false;
	}
}
