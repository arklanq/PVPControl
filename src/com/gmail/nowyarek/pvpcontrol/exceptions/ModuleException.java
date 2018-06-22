package com.gmail.nowyarek.pvpcontrol.exceptions;

public class ModuleException extends Exception {

	public ModuleException(Exception e, String module){
		super("Module detected an unknown error. Send this stack trace to @IdkMan on spigot:");
		e.printStackTrace();
	}
	
	public ModuleException(Exception e, String module, String msg){
		
		super("Module detected an unknown error. Send this stack trace to @IdkMan on spigot:"
				+ "Additional info: "+msg);
		e.printStackTrace();
				
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2996367501046200145L;

}
