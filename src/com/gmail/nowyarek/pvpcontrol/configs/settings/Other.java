package com.gmail.nowyarek.pvpcontrol.configs.settings;

import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import com.gmail.nowyarek.pvpcontrol.configs.CheckSectionExpeller;

public class Other extends SettingsSubSection {
	private List<String> logOutCmds, blockedCmds;
	
	public List<String> getListOfLogOutCommands(){
		return this.logOutCmds;
	}
	public List<String> getListOfBlockedCommands(){
		return this.blockedCmds;
	}

	protected Other(ConfigurationSection parentSection, ConfigurationSection defaults) {
		super(parentSection, defaults, "Other");
		this.checkSection();
	}

	@Override
	protected void checkSection() {
		if(!section.isSet("CommandsExecutedWhenLogoutInPVP")) return;
		try {
			this.logOutCmds = section.getStringList("CommandsExecutedWhenLogoutInPVP");
		} catch(Exception e) {
			CheckSectionExpeller.signalConfigurationError(this, "CommandsExecutedWhenLogoutInPVP");
			this.logOutCmds = defaults.getStringList("CommandsExecutedWhenLogoutInPVP");
		}
		if(!section.isSet("BlockOnlySelectedCommandsOnPVP")) return;
		try {
			this.blockedCmds = section.getStringList("BlockOnlySelectedCommandsOnPVP");
			for(int i=0; i<blockedCmds.size(); i++) {
				String cmd = blockedCmds.get(i);
				if(cmd.charAt(0)=='/') {
					blockedCmds.set(i, cmd.substring(1));
				}
			}
		} catch(Exception e) {
			CheckSectionExpeller.signalConfigurationError(this, "BlockOnlySelectedCommandsOnPVP");
			this.blockedCmds = defaults.getStringList("BlockOnlySelectedCommandsOnPVP");
		}
	}

}
