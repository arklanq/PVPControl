package com.gmail.nowyarek.pvpcontrol.basic;

import java.util.HashMap;
import java.util.Map;

public class Variables {
	Map<String, String> variables = new HashMap<String, String>();
	
	public Variables(){
		
	}
	public Variables(String variable, String toReplace){
		variables.put(variable, toReplace);
	}
	public void addVariable(String variable, String toReplace){
		variables.put(variable, toReplace);
	}
}
