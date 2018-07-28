package com.gmail.nowyarek.pvpcontrol.modules.dmghandler;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.gmail.nowyarek.pvpcontrol.basic.Msg;
import com.gmail.nowyarek.pvpcontrol.basic.Text;
import com.gmail.nowyarek.pvpcontrol.configs.ConfigsAccess;
import com.gmail.nowyarek.pvpcontrol.integration.DependencyType;
import com.gmail.nowyarek.pvpcontrol.integration.Integration;

class EssentialsGodModeAddon implements EntityDmgHandlerAddon {
	private Essentials essentials = null;
	
	EssentialsGodModeAddon(ConfigsAccess configsAccess, Integration integration) {
		if(!configsAccess.settings.integrationSection.isDisableEssentialsGodModeOnHit())
			return;
		if(!integration.check(DependencyType.ESSENTIALSX)) {
			//say
			return;
		}
		essentials = integration.getEssentials();
		if(essentials==null) {
			//say
			return;
		}
	}

	@Override
	public boolean shouldBeUsed() {
		return essentials!=null;
	}

	@Override
	public EntityDmgHandlerAddonPriority getPriority() {
		return EntityDmgHandlerAddonPriority.BEFORE_CANCEL;
	}

	@Override
	public void run(EntityDamageByEntityEvent e, Player victim, Player damager) {
		User user = essentials.getUser(victim.getUniqueId());
		if(user.isGodModeEnabled()) {
			user.setGodModeEnabled(false);
			e.setCancelled(false);
			victim.sendMessage(Msg.info(Text.GOD_MODE_HAS_BEEN_DISABLED));
		}
	}

}
