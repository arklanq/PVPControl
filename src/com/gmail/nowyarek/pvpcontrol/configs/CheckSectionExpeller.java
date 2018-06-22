package com.gmail.nowyarek.pvpcontrol.configs;

import com.gmail.nowyarek.pvpcontrol.basic.Msg;
import com.gmail.nowyarek.pvpcontrol.configs.settings.SettingsSubSection;


public class CheckSectionExpeller {
	
	public static void signalConfigurationError(SettingsSubSection section, String field) {
		Msg.serverError("File configuration settings.yml is filled incorrectly: "
				+"\n"+section.getPath()+" -> "+field);
	}
	
	public static void signalConfigurationError(SettingsSubSection section, String field, String msg) {
		Msg.serverError("File configuration settings.yml is filled incorrectly: "
				+"\n"+section.getPath()+" -> "+field
				+"\n"+msg);
	}
	
	/*public static void signalConfigurationError(ConfigSubSection section, String field) {
		Msg.serverWarn("Plik konfiguracyjny settings.yml jest wypelniony blednie: "
				+"\n"+section.toString()+" -> "+field);
	}*/
	
}
