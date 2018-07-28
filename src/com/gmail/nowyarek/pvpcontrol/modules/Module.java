package com.gmail.nowyarek.pvpcontrol.modules;

import com.gmail.nowyarek.pvpcontrol.exceptions.ModuleException;

public abstract class Module {
	protected boolean enabled;

	public abstract void onEnable() throws ModuleException;
	public abstract void onDisable() throws ModuleException;
	public abstract ModuleType getType();
	public abstract boolean shouldBeEnabled();
	public boolean isEnabled() {
		return this.enabled;
	}
	
}
