package com.gmail.nowyarek.pvpcontrol.basic;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

import com.gmail.nowyarek.pvpcontrol.logs.LogsManager;


public class Msg {
	private static ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
	private static String PLUGIN_PREFIX;
	private static ArrayList<PreloadedMessage> preloaded_msgs_to_say = new ArrayList<PreloadedMessage>();
	private static String CONSOLE_LOG_PREFIX, ERROR_PREFIX, WARNING_PREFIX, INFO_PREFIX, ANNOUNCEMENT_PREFIX, DEBUG_PREFIX;
	private static TextManager manager;
	private static LogsManager logsManager;
	
	public void InitalizeMsg(FileManager filemanager){
		manager = new TextManager(filemanager);
		manager.checkLayout(); initializePrefixes();
		sayAllPreloadedMessages();
		
		logsManager = filemanager.getLogsManager();
		logsManager.enable();
	}
	
	public static LogsManager getLogsManager() {
		return logsManager;
	}
	
	public void reload(boolean reloadFileManagerFile){
		manager.reload(reloadFileManagerFile);
		manager.checkLayout(); initializePrefixes();
	}
	
	private void initializePrefixes(){
		PLUGIN_PREFIX = TextManager.Say(Text.PLUGIN_PREFIX);
		CONSOLE_LOG_PREFIX = PLUGIN_PREFIX + TextManager.Say(Text.CONSOLE_LOG_COLOR_PREFIX);
		ERROR_PREFIX = PLUGIN_PREFIX + TextManager.Say(Text.ERROR_COLOR_PREFIX);
		WARNING_PREFIX = PLUGIN_PREFIX + TextManager.Say(Text.WARNING_COLOR_PREFIX);
		INFO_PREFIX = PLUGIN_PREFIX + TextManager.Say(Text.INFO_COLOR_PREFIX);
		ANNOUNCEMENT_PREFIX = PLUGIN_PREFIX + TextManager.Say(Text.ANNOUNCEMENT_COLOR_PREFIX);
		DEBUG_PREFIX = PLUGIN_PREFIX + TextManager.Say(Text.DEBUG_COLOR_PREFIX);
	}
	
	void addPreloadMessageToSayLater(PreloadedMessage pmsg){
		preloaded_msgs_to_say.add(pmsg);
	}
	
	private void sayAllPreloadedMessages(){
		for(PreloadedMessagePriority priority : PreloadedMessagePriority.values()) {
			for(PreloadedMessage pmsg : preloaded_msgs_to_say){
				if(!pmsg.prior.equals(priority)) continue;
				if(pmsg.var==null) serverError(pmsg.text);
				else serverError(pmsg.text, pmsg.var);
			}
		}
		
		preloaded_msgs_to_say.clear();
	}
	
	public static void debug(String message){
		console.sendMessage(DEBUG_PREFIX + message);
	}
	public static void consoleLog(Text text){
		console.sendMessage(CONSOLE_LOG_PREFIX + TextManager.Say(text));
	}
	public static void consoleLog(Text text, Variables var){
		console.sendMessage(CONSOLE_LOG_PREFIX + TextManager.Say(text, var));
	}
	public static void consoleLog(String message){
		console.sendMessage(CONSOLE_LOG_PREFIX + message);
	}
	public static void serverError(Text text){
		String msg = TextManager.Say(text);
		console.sendMessage(ERROR_PREFIX + msg);
		if(logsManager!=null) logsManager.logError(msg);
	}
	public static void serverError(Text text, Variables var){
		String msg = TextManager.Say(text, var);
		console.sendMessage(ERROR_PREFIX + msg);
		if(logsManager!=null) logsManager.logError(msg);
	}
	public static void serverError(String message){
		console.sendMessage(ERROR_PREFIX + message);
		if(logsManager!=null) logsManager.logError(message);
	}
	public static String error(Text text){
		serverError(text);
		return ERROR_PREFIX + TextManager.Say(text);
	}
	public static String error(Text text, Variables var){
		serverError(text, var);
		return ERROR_PREFIX + TextManager.Say(text, var);
	}
	public static String error(String message){
		serverError(message);
		return ERROR_PREFIX + message;
	}
	public static String warn(Text text){
		return WARNING_PREFIX + TextManager.Say(text);
	}
	public static String warn(Text text, Variables var){
		return WARNING_PREFIX + TextManager.Say(text, var);
	}
	public static String warn(String message){
		return WARNING_PREFIX + message;
	}
	public static void serverWarn(Text text){
		console.sendMessage(WARNING_PREFIX + TextManager.Say(text));
	}
	public static void serverWarn(Text text, Variables var){
		console.sendMessage(WARNING_PREFIX + TextManager.Say(text, var));
	}
	public static void serverWarn(String message){
		console.sendMessage(WARNING_PREFIX + message);
	}
	public static String info(Text text){
		return INFO_PREFIX + TextManager.Say(text);
	}
	public static String info(Text text, Variables var){
		return INFO_PREFIX + TextManager.Say(text, var);
	}
	public static String info(String message){
		return INFO_PREFIX + message;
	}
	public static void serverInfo(Text text){
		console.sendMessage(INFO_PREFIX + TextManager.Say(text));
	}
	public static void serverInfo(Text text, Variables var){
		console.sendMessage(INFO_PREFIX + TextManager.Say(text, var));
	}
	public static void serverInfo(String message){
		console.sendMessage(INFO_PREFIX + message);
	}
	public static String annoucement(Text text){
		return ANNOUNCEMENT_PREFIX + TextManager.Say(text);
	}
	public static String annoucement(Text text, Variables var){
		return ANNOUNCEMENT_PREFIX + TextManager.Say(text, var);
	}
	public static String annoucement(String message){
		return ANNOUNCEMENT_PREFIX + message;
	}
	public static void serverAnnoucement(Text text){
		console.sendMessage(ANNOUNCEMENT_PREFIX + TextManager.Say(text));
	}
	public static void serverAnnoucement(Text text, Variables var){
		console.sendMessage(ANNOUNCEMENT_PREFIX + TextManager.Say(text, var));
	}
	public static void serverAnnoucement(String message){
		console.sendMessage(ANNOUNCEMENT_PREFIX + message);
	}
}

class PreloadedMessage {
	Text text;
	Variables var;
	PreloadedMessagePriority prior;
	PreloadedMessage(Text text, PreloadedMessagePriority priority){
		this.text = text;
		this.prior = priority;
	}
	PreloadedMessage(Text text, Variables var, PreloadedMessagePriority priority){
		this.text = text;
		this.var = var;
		this.prior = priority;
	}
}
enum PreloadedMessagePriority{
	FIRST, EARLY, NORMAL, LATER, LAST
}
//==================================================================== OLD =============================================================
/*private void checkLanguageStatements(TextManager manager){
int i = manager.checkLayout();
initializePrefixes();
//OLD
if(Text.values().length>i){
	Error(Text.MISSING_LANGUAGE_STATEMENTS, new Variables("%number%", ""+(Text.values().length-i)));
}
ConsoleLog(Text.LANGUAGE_STATEMENTS_LOADED, new Variables("%number%", ""+i));
}*/