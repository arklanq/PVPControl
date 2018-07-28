package com.gmail.nowyarek.pvpcontrol.modules;

import com.gmail.nowyarek.pvpcontrol.PVPCore;
import com.gmail.nowyarek.pvpcontrol.exceptions.ModuleException;
import com.gmail.nowyarek.pvpcontrol.modules.dmghandler.EntityDamageHandler;
import com.gmail.nowyarek.pvpcontrol.modules.other.PlayerChatHandler;
import com.gmail.nowyarek.pvpcontrol.modules.other.PlayerDeathHandler;
import com.gmail.nowyarek.pvpcontrol.modules.other.PlayerLogOutHandler;
import com.gmail.nowyarek.pvpcontrol.modules.other.PlayerTeleportHandler;

class ModulesInstancesGetters {
	private PVPCore core;
	
	ModulesInstancesGetters(PVPCore core) {
		this.core = core;
	}

	Module getNewInstanceOfModule(ModuleType type) throws Exception {
		try {
			switch(type) {
			case ENTITY_DAMAGE_HANDLER:
				return new EntityDamageHandler(
						core
						);
			case PLAYER_CHAT_HANDLER:
				return new PlayerChatHandler(
						core.getPlugin(),
						core.getConfigsAccess(),
						core.getPVPModeHandler()
						);
			case PLAYER_DEATH_HANDLER:
				return new PlayerDeathHandler(
						core.getPlugin(),
						core.getPVPModeHandler()
						);
			case PLAYER_LOG_OUT_HANDLER:
				return new PlayerLogOutHandler(
						core.getPlugin(),
						core.getConfigsAccess(),
						core.getPVPModeHandler()
						);
			case PLAYER_TELEPORT_HANDLER:
				return new PlayerTeleportHandler(
						core.getPlugin(),
						core.getConfigsAccess(),
						core.getPVPModeHandler()
						);
			default:
				return null;
			
			}
		} catch(Exception e) {
			throw new ModuleException(type, e);
		}
	}
 	
}
