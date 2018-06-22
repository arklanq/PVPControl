package com.gmail.nowyarek.pvpcontrol.commands.pvpc;

import java.util.Comparator;

import com.gmail.nowyarek.pvpcontrol.basic.Msg;
import com.gmail.nowyarek.pvpcontrol.basic.Text;
import com.gmail.nowyarek.pvpcontrol.basic.TextManager;
import com.gmail.nowyarek.pvpcontrol.basic.Variables;

public class HelpPrinter {
	private final int args_per_page = 6;
	private String[] pages;
	private PvpcCommand pvpcCommand;
	
	public HelpPrinter(PvpcCommand pvpcCommand) {
		this.pvpcCommand = pvpcCommand;
	}
	
	public void initalizeHelp(){
		int args_size = pvpcCommand.subcommands.size();
		pvpcCommand.subcommands.sort(new CustomComparator());
		pages = new String[1+ ((args_size-1)/args_per_page) ];
		for(int i=0; i<pages.length; i++){
			StringBuilder sb = new StringBuilder();
			sb.append(TextManager.Say(Text.THEMATIC_BREAK) + "\n");
			sb.append("§6"+TextManager.Say(Text.HELP_TITLE, new Variables("%page%", ""+(i+1))) + "\n");
			for(int entry_on_page=0; entry_on_page<args_per_page; entry_on_page++){
				int index = i*args_per_page+entry_on_page;
				if(index>=args_size) continue;
				
				sb.append("§6"+TextManager.Say(pvpcCommand.subcommands.get(index).getArgumentUsage()) + "\n");
			}
			if(i==pages.length-1){
				sb.append("§e"+TextManager.Say(Text.HELP_LAST_PAGE_INFO));
			}else{
				sb.append("§e"+TextManager.Say(Text.HELP_NEXT_PAGE_INFO, new Variables("%next_page%", ""+(i+2))));
			}
			pages[i] = sb.toString();
		}
	}
	
	public void reload(){
		this.initalizeHelp();
	}
	
	public String printHelp(int i){
		String help;
		try{
			help = pages[i];
		}catch(IndexOutOfBoundsException e){
			help = Msg.warn(Text.HELP_INCORRECT_PAGE, new Variables("%usage%", TextManager.Say(Text.COMMAND_HELP_USAGE)));
		}
		return help;
	}
}

class CustomComparator implements Comparator<PvpcSubCommand> {

	@Override
	public int compare(PvpcSubCommand arg0, PvpcSubCommand arg1) {
		return TextManager.Say(arg0.getArgumentName()).compareToIgnoreCase(TextManager.Say(arg1.getArgumentName()));
	}
}
