package com.gmail.nowyarek.pvpcontrol.commands.pvpc;

import org.bukkit.command.CommandSender;

import com.gmail.nowyarek.pvpcontrol.basic.Msg;
import com.gmail.nowyarek.pvpcontrol.basic.Text;
import com.gmail.nowyarek.pvpcontrol.basic.TextManager;
import com.gmail.nowyarek.pvpcontrol.basic.Variables;

public class Help extends PvpcSubCommand {
	private final static String permission = "pvpc.command.pvpc.help";
	private HelpPrinter helpPrinter;

	public Help(HelpPrinter helpPrinter) {
		this.helpPrinter = helpPrinter;
	}

	@Override
	public Text getArgumentName() {
		return Text.COMMAND_HELP;
	}

	@Override
	public Text getArgumentAliasName() {
		return Text.COMMAND_HELP_ALIAS;
	}

	@Override
	public Text getArgumentUsage() {
		return Text.COMMAND_HELP_USAGE;
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
		if(args.length==0) {
			sender.sendMessage(helpPrinter.printHelp(0));
		} else {
			int page;
			try {
				page = Integer.parseInt(args[0]);
				page--;
			} catch(NumberFormatException e) {
				sender.sendMessage(Msg.warn(Text.HELP_INCORRECT_PAGE, new Variables("%usage%", TextManager.Say(Text.COMMAND_HELP_USAGE))));
				return;
			}
			sender.sendMessage(helpPrinter.printHelp(page));
		}
	}

}
