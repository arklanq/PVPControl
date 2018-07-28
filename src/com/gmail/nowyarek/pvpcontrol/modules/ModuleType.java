package com.gmail.nowyarek.pvpcontrol.modules;

public enum ModuleType {
	ENTITY_DAMAGE_HANDLER(ValidityOfModule.ESSENTIAL),
	PLAYER_DEATH_HANDLER(ValidityOfModule.ESSENTIAL),
	PLAYER_LOG_OUT_HANDLER(ValidityOfModule.ESSENTIAL),
	PLAYER_TELEPORT_HANDLER,
	PLAYER_CHAT_HANDLER;
	
	private ValidityOfModule validity;
	
	private ModuleType() {
		this.validity = ValidityOfModule.OPTIONAL;
	}
	private ModuleType(ValidityOfModule validity) {
		this.validity = validity;
	}

	public ValidityOfModule getValidityOfModule() {
		return validity;
	}
}
