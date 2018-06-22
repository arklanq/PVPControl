package com.gmail.nowyarek.pvpcontrol.exceptions;

import com.gmail.nowyarek.pvpcontrol.basic.Msg;

public class PVPSoftException extends RuntimeException {
	
	public PVPSoftException() {
		Msg.serverWarn("Found error..");
	}
	
	public PVPSoftException(String msg) {
		Msg.serverWarn("Found error..\n"
				+"Additional info: " + msg);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -8097507394079736927L;

}
