package com.gmail.nowyarek.pvpcontrol.commands.pvpc;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.nowyarek.pvpcontrol.PVPCore;
import com.gmail.nowyarek.pvpcontrol.basic.Msg;
import com.gmail.nowyarek.pvpcontrol.basic.Text;
import com.gmail.nowyarek.pvpcontrol.basic.TextManager;

public class PvpcCommand implements CommandExecutor, Listener {
	private final static String permission = "pvpc.command.pvpc";
	public ArrayList<PvpcSubCommand> subcommands = new ArrayList<PvpcSubCommand>();
	private Plugin plugin;
	private HelpPrinter helpPrinter;
	
	public PvpcCommand(PVPCore pvpCore) {
		this.plugin = pvpCore.getPlugin();
		
		helpPrinter = new HelpPrinter(this);
		
		subcommands.add(new Reload(pvpCore.getReloader()));
		subcommands.add(new Help(helpPrinter));

		helpPrinter.initalizeHelp();
		
		((JavaPlugin) plugin).getCommand("pvpc").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("pvpc")) return true;
		if(!sender.hasPermission(permission)){
			sender.sendMessage(Msg.warn(Text.COMMAND_NO_PERMISSIONS));
			return false;
		}
		if(args.length>=1){
			String[] subcommands_args = new String[args.length-1];
			for(int i=1; i<args.length; i++){
				subcommands_args[i-1] = args[i]; 
			}
			for(PvpcSubCommand subCommand : subcommands){
				if(args[0].equalsIgnoreCase(TextManager.Say(subCommand.getArgumentName()))){
					this.runSubCommand(sender, subCommand, label, subcommands_args);
					return true;
				}
				if(TextManager.available(subCommand.getArgumentAliasName())
						&& args[0].equalsIgnoreCase(TextManager.Say(subCommand.getArgumentAliasName()))){
					this.runSubCommand(sender, subCommand, label, subcommands_args);
					return true;
				}
			}
		}
		sender.sendMessage(helpPrinter.printHelp(0));
		return true;
	}
	
	private void runSubCommand(CommandSender sender, PvpcSubCommand subCommand, String label, String[] args){
		Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, new Runnable(){
			@Override
			public void run() {
				subCommand.execute(sender, subCommand.getArgumentName(), label, args);
			}
		}, 0);
	}
	
	public HelpPrinter getHelpPrinter() {
		return this.helpPrinter;
	}
	
}
