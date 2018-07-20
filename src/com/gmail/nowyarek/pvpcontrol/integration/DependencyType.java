package com.gmail.nowyarek.pvpcontrol.integration;

public enum DependencyType {
	ESSENTIALSX;
	
	public String getEasyName(){
		switch(this) {
		case ESSENTIALSX:
			return "EssentialsX";
		default:
			return "Unknown name";
		}
	}
	
	String getPluginYMLName() {
		switch(this) {
		case ESSENTIALSX:
			return "Essentials";
		default:
			return "UnknownName";
		}
	}
	
}
