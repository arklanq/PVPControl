package com.gmail.nowyarek.pvpcontrol.exceptions;

import com.gmail.nowyarek.pvpcontrol.modules.Module;
import com.gmail.nowyarek.pvpcontrol.modules.ModuleType;

public class ModuleException extends Exception {
	
	public ModuleException(Module module){
		this(module.getType());
	}

	public ModuleException(Module module, Exception e){
		this(module.getType(), e);
	}
	
	public ModuleException(Module module, Exception e, String msg){
		this(module.getType(), e, msg);
	}
	
	public ModuleException(ModuleType module){
		super("PVPControl detected an unknown error with "+module.toString()+" module. Send console logs to @IdkMan on spigot:");
	}

	public ModuleException(ModuleType module, Exception e){
		super("PVPControl detected an unknown error with "+module.toString()+" module. Send this error log to @IdkMan on spigot:");
	}
	
	public ModuleException(ModuleType module, Exception e, String msg){
		super("PVPControl detected an unknown error with "+module.toString()+" module. Send this error log to @IdkMan on spigot:"
				+ "Additional info: "+msg);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2996367501046200145L;

}
