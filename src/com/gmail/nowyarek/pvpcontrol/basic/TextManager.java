package com.gmail.nowyarek.pvpcontrol.basic;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

public class TextManager{
	private FileManager filemanager;
	private static FileConfiguration msgsFile;
	
	TextManager(){ }
	
	TextManager(FileManager filemanager){
		this.filemanager = filemanager;
		msgsFile = filemanager.getMsgsLayout();
	}
	
	public void reload(boolean reloadFileManagerFile){
		if(reloadFileManagerFile){
			filemanager.reloadMsgsLayout();
		}
		msgsFile = filemanager.getMsgsLayout();
	}
	int checkLayout(){
		int i=0;
		for(Text text : Text.values()){
			String msg = msgsFile.getString(text.toString());
			if(msg==null){
				Bukkit.getConsoleSender().sendMessage(Msg.plugin_prefix+"§4"+text.toString()+" does not exist in built-in language file.");
				continue;
			}
			msg = msg.replaceAll("&", "§");
			msgsFile.set(text.toString(), msg);
			i++;
		}
		return i;
	}
	
	public static String Say(Text text){
		String msg = msgsFile.getString(text.toString());
		if(msg == null) msg = "§3Error, unknow translation: §f"+text.toString()+"\n";
		return msg;
	}
	public static String Say(Text text, Variables var){
		String msg = msgsFile.getString(text.toString());
		if(msg == null) msg = "§3Error, unknow translation: §f"+text.toString();
		msg = checkForVariables(msg, var);
		return msg;
	}
	public static boolean available(Text text){
		String msg = msgsFile.getString(text.toString());
		if(msg.startsWith("§3Error, unknow translation: §f")) return false;
		else return true;
	}
	
	private static String checkForVariables(String msg, Variables var){
		Map<String, String> variables = var.variables;
		for(String variable : variables.keySet()){
			if(msg.contains(variable)){
				msg = msg.replaceAll(variable, (variables.get(variable)!=null) ? variables.get(variable) : " ");
			}
		}
		return msg;
	}
}









