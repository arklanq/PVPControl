package com.gmail.nowyarek.pvpcontrol.commands.pvpc;


import org.bukkit.command.CommandSender;

import com.gmail.nowyarek.pvpcontrol.basic.Text;
import com.gmail.nowyarek.pvpcontrol.basic.TextManager;
import com.gmail.nowyarek.pvpcontrol.basic.Variables;


public abstract class PvpcSubCommand {
	
	public abstract Text getArgumentName();
	public abstract Text getArgumentAliasName();
	public abstract Text getArgumentUsage();
	public abstract String getPermission();
	public abstract void execute(CommandSender sender, Text argument, String alias, String[] args);
	
	protected Variables getVariables() {
		Variables var = new Variables();
		var.addVariable("%argument%", TextManager.Say(getArgumentName()));
		var.addVariable("%alias%", TextManager.Say(getArgumentAliasName()));
		var.addVariable("%usage%", TextManager.Say(getArgumentUsage()));
		var.addVariable("%permission%", getPermission());
		return var;
	}
	
}




