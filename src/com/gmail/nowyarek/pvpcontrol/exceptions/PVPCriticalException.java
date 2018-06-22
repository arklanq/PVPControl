package com.gmail.nowyarek.pvpcontrol.exceptions;

import com.gmail.nowyarek.pvpcontrol.basic.Msg;

public class PVPCriticalException extends Exception {
	
	public PVPCriticalException() {
		Msg.serverError("Found critical error, disabling plugin..");
	}
	
	public PVPCriticalException(String msg) {
		Msg.serverError("Found critical error, disabling plugin..\n"
				+"Additional info: " + msg);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -4048705398849082608L;

}
