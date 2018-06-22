package com.gmail.nowyarek.pvpcontrol.commands.pvp;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.nowyarek.pvpcontrol.basic.Msg;
import com.gmail.nowyarek.pvpcontrol.basic.Text;
import com.gmail.nowyarek.pvpcontrol.basic.Variables;
import com.gmail.nowyarek.pvpcontrol.pvpmode.PVPModeHandler;

public class PVPCommand implements Listener, CommandExecutor {
	private Plugin plugin;
	private PVPModeHandler pvpModeHandler;
	
	public PVPCommand(Plugin plugin, PVPModeHandler pvpModeHandler) {
		this.plugin = plugin;
		this.pvpModeHandler = pvpModeHandler;
		((JavaPlugin) plugin).getCommand("pvp").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("pvp")) return false;
		if(!(sender instanceof Player)) {
			sender.sendMessage(Msg.warn(Text.COMMAND_NOT_ALLOWED_FOR_CONSOLE));
			return false;
		}
		Player p = (Player) sender;
		if(sender.hasPermission("pvpc.command.pvp.admin")) {
			if(!p.hasMetadata("pvpmode.admin")) {
				//turn off pvp mode
				p.setMetadata("pvpmode.admin", new FixedMetadataValue(plugin, true));
				p.sendMessage(Msg.info(Text.PVP_MODE_OFF));
			} else {
				//turn on pvp mode
				p.removeMetadata("pvpmode.admin", plugin);
				p.sendMessage(Msg.info(Text.PVP_MODE_ON));
			}
			return true;
		} else if(sender.hasPermission("pvpc.command.pvp")) {
			if(pvpModeHandler.isPlayerInCombat(p.getUniqueId())) {
				Variables var = new Variables("%seconds%", ""+getSecondsLeft(p.getUniqueId()));
				p.sendMessage(Msg.info(Text.YOU_ARE_STILL_ON_PVP, var));
			} else {
				p.sendMessage(Msg.info(Text.YOU_ARE_NOT_ON_PVP));
			}
			return true;
		} else {
			sender.sendMessage(Msg.warn(Text.COMMAND_NO_PERMISSIONS));
			return false;
		}
	}
	
	private double getSecondsLeft(UUID uuid) {
		long milisLeft = (pvpModeHandler.timeInPVP*1000-(System.currentTimeMillis()-pvpModeHandler.getPlayerPVPStartTime(uuid)));
		return Math.round((double)(milisLeft/1000));
	}
	
}
