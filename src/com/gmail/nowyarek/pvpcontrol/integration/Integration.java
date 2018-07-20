package com.gmail.nowyarek.pvpcontrol.integration;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import com.earth2me.essentials.Essentials;
import com.gmail.nowyarek.pvpcontrol.basic.Msg;
import com.gmail.nowyarek.pvpcontrol.basic.Text;
import com.gmail.nowyarek.pvpcontrol.basic.Variables;

public class Integration {
	private Map<DependencyType, Boolean> integrationMap = new HashMap<DependencyType, Boolean>();
	private PluginManager manager;
	
	private Essentials essentials;
	
	public Integration(Plugin plugin) {
		this.manager = plugin.getServer().getPluginManager();
	}
	
	public boolean check(DependencyType pl){
		return integrationMap.get(pl);
	}
	
	public void checkDependency() {
		Msg.consoleLog(Text.DEPENDENCY_CHECKING);
		for(DependencyType pl : DependencyType.values()){
			integrationMap.put(pl, false);
		}
		
		///////////////////////////////////////////////////////////////
		for(DependencyType dependency : DependencyType.values()) {
			if(manager.getPlugin(dependency.getPluginYMLName()) != null){
				integrationMap.put(dependency, true);
			}
		}
		
		initializeIntegratedPlugins();
		
		///////////////////////////////////////////////////////////////
		
		StringBuilder sb = new StringBuilder();
		boolean swapColor = true;
		for(DependencyType pl : integrationMap.keySet()){
			if(integrationMap.get(pl)) sb.append(" " + (swapColor ? ChatColor.GREEN : ChatColor.DARK_GREEN) + pl.getEasyName());
			if(swapColor)
				swapColor = false;
			else
				swapColor = true;
		}
		if(sb.toString()==null || sb.toString().equals("")) return;
		String msg = sb.toString(); msg = msg.substring(1);
		Msg.consoleLog(Text.PLUGINS_HOOKED, new Variables("%plugins%", msg));
	}
	
	private void initializeIntegratedPlugins() {
		if(check(DependencyType.ESSENTIALSX)) {
			if(manager.getPlugin(DependencyType.ESSENTIALSX.getPluginYMLName()) instanceof Essentials) {
				this.essentials = (Essentials) manager.getPlugin(DependencyType.ESSENTIALSX.getPluginYMLName());
			} else {
				// say - plugin not recognized
				this.integrationMap.put(DependencyType.ESSENTIALSX, false);
				Msg.serverWarn(Text.PLUGIN_HOOKED_BUT_WAS_NOT_RECOGNIZED, new Variables("%plugin%", DependencyType.ESSENTIALSX.getEasyName()));
			}
		}
	}

	public Essentials getEssentials() {
		return essentials;
	}
	
	
	
}











