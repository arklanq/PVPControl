package com.gmail.nowyarek.pvpcontrol.modules.dmghandler;

import java.util.ArrayList;

import com.gmail.nowyarek.pvpcontrol.PVPCore;

class EntityDmgHandlerAddonsManager {
	private ArrayList<EntityDmgHandlerAddon> cancellationSensitiveAddons = new ArrayList<EntityDmgHandlerAddon>();
	private ArrayList<EntityDmgHandlerAddon> ignoreCancellationAddons = new ArrayList<EntityDmgHandlerAddon>();
	
	EntityDmgHandlerAddonsManager(PVPCore core) {
		ArrayList<EntityDmgHandlerAddon> addons = new ArrayList<EntityDmgHandlerAddon>();
		addons.add(new EssentialsGodModeAddon(core.getConfigsAccess(), core.getIntegration()));
		addons.add(new FlyAddon(core.getConfigsAccess()));
		addons.add(new InvisibilityAddon(core.getConfigsAccess()));
		
		
		for(EntityDmgHandlerAddon addon : addons) {
			if(addon.shouldBeUsed()) {
				if(addon.getPriority()==EntityDmgHandlerAddonPriority.BEFORE_CANCEL)
					cancellationSensitiveAddons.add(addon);
				else
					ignoreCancellationAddons.add(addon);
			}
		}
	}

	ArrayList<EntityDmgHandlerAddon> getCancellationSensitiveAddons() {
		return cancellationSensitiveAddons;
	}

	ArrayList<EntityDmgHandlerAddon> getIgnoreCancellationAddons() {
		return ignoreCancellationAddons;
	}
	
	
}
