package com.gmail.nowyarek.pvpcontrol.commands.pvpc;

import org.bukkit.command.CommandSender;

import com.gmail.nowyarek.pvpcontrol.basic.Msg;
import com.gmail.nowyarek.pvpcontrol.basic.Reloader;
import com.gmail.nowyarek.pvpcontrol.basic.Text;

public class Reload extends PvpcSubCommand {
	private final static String permission = "pvpc.command.pvpc.reload";
	private Reloader reloader;

	public Reload(Reloader reloader) {
		this.reloader = reloader;
	}

	@Override
	public Text getArgumentName() {
		return Text.COMMAND_RELOAD;
	}

	@Override
	public Text getArgumentAliasName() {
		return Text.COMMAND_RELOAD_ALIAS;
	}

	@Override
	public Text getArgumentUsage() {
		return Text.COMMAND_RELOAD_USAGE;
	}

	@Override
	public String getPermission() {
		return permission;
	}

	@Override
	public void execute(CommandSender sender, Text argument, String alias, String[] args) {
		if(!sender.hasPermission(permission)) {
			sender.sendMessage(Msg.warn(Text.COMMAND_NO_PERMISSIONS));
			return;
		}
		try {
			reloader.reloadWholeConfiguration();
		} catch(Exception e) {
			sender.sendMessage(Msg.error("Something went wrong.."));
			return;
		}
		sender.sendMessage(Msg.annoucement(Text.RELOAD_COMPLETE));
	}

}
