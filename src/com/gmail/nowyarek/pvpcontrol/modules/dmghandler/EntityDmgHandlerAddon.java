package com.gmail.nowyarek.pvpcontrol.modules.dmghandler;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

interface EntityDmgHandlerAddon {

	boolean shouldBeUsed();
	default EntityDmgHandlerAddonPriority getPriority() {
		return EntityDmgHandlerAddonPriority.AFTER_CANCEL;
	}
	void run(EntityDamageByEntityEvent e, Player victim, Player damager);
	
}

enum EntityDmgHandlerAddonPriority {
	BEFORE_CANCEL, AFTER_CANCEL
}
