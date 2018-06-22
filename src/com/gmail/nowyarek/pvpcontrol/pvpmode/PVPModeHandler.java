package com.gmail.nowyarek.pvpcontrol.pvpmode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.gmail.nowyarek.pvpcontrol.basic.Msg;
import com.gmail.nowyarek.pvpcontrol.basic.Text;
import com.gmail.nowyarek.pvpcontrol.configs.ConfigsAccess;

public class PVPModeHandler {
	protected Plugin plugin;
	protected ConfigsAccess configsAccess;
	protected static HashMap<UUID, Long> playersInCombat = new HashMap<UUID, Long>();
	public short timeInPVP;
	protected short checkingSpeed;
	protected BukkitTask checkingTask;
	
	public PVPModeHandler(Plugin plugin, ConfigsAccess configsAccess) {
		this.plugin = plugin;
		this.configsAccess = configsAccess;
		this.timeInPVP = configsAccess.settings.pvp.getTimeInPVPMode();
		this.checkingSpeed = configsAccess.settings.performance.getCombatModeCheckSpeed();
		this.checkingTask = new CombatModeChecker().runTaskTimerAsynchronously(plugin, checkingSpeed, checkingSpeed);
	}
	
	public void stop() {
		if(checkingTask!=null) {
			checkingTask.cancel();
			checkingTask = null;
			playersInCombat.clear();
		}
	}
	
	public List<UUID> getAllPlayersInPVP(){
		List<UUID> list = new ArrayList<UUID>(playersInCombat.size());
		for(UUID uuid : playersInCombat.keySet()) {
			list.add(uuid);
		}
		return list;
	}
	
	public HashMap<UUID, Long> getAllPlayerInPVPWithTimings(){
		return playersInCombat;
	}
	
	public long getPlayerPVPStartTime(UUID uuid) {
		for(UUID temp_uuid : playersInCombat.keySet()) {
			if(temp_uuid.compareTo(uuid)==0) return playersInCombat.get(uuid);
		}
		return 0;
	}
	
	public void updatePlayerPVPStartTime(UUID uuid) {
		playersInCombat.put(uuid, System.currentTimeMillis());
	}
	
	public boolean isPlayerInCombat(UUID playerUUID) {
		for(UUID uuid : playersInCombat.keySet()) {
			if(uuid.compareTo(playerUUID)==0) return true;
		}
		return false;
	}
	
	public void turnOnCombatModeForPlayer(Player player) {
		playersInCombat.put(player.getUniqueId(), System.currentTimeMillis());
		//say
		player.sendMessage(Msg.info(Text.YOU_ARE_IN_PVP));
	}
	
	public void turnOffCombatModeForPlayer(UUID uuid) {
		playersInCombat.remove(uuid);
		//say
		Player p = plugin.getServer().getPlayer(uuid);
		if(p==null) return;
		p.sendMessage(Msg.info(Text.YOU_ARE_NOT_IN_PVP));
	}
	
	public void reload(boolean reloadConfigsAccess) {
		if(reloadConfigsAccess) configsAccess.settings.reload(true);
		this.timeInPVP = configsAccess.settings.pvp.getTimeInPVPMode();
		this.checkingSpeed = configsAccess.settings.performance.getCombatModeCheckSpeed();
		if(this.checkingTask!=null) {
			this.checkingTask.cancel();
			this.checkingTask = null;
		}
		this.checkingTask = new CombatModeChecker().runTaskTimerAsynchronously(plugin, checkingSpeed, checkingSpeed);
	}
	
	
	class CombatModeChecker extends BukkitRunnable {

		@Override
		public void run() {
			long timeMilis = System.currentTimeMillis();
			ArrayList<UUID> playersToRemove = new ArrayList<UUID>();
			for(UUID uuid : PVPModeHandler.playersInCombat.keySet()) {
				if((PVPModeHandler.playersInCombat.get(uuid)+timeInPVP*1000) < timeMilis) {
					playersToRemove.add(uuid);
				}
			}
			
			for(UUID uuid : playersToRemove) {
				turnOffCombatModeForPlayer(uuid);
			}
		}
		
	}
	
	
}

