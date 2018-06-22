package com.gmail.nowyarek.pvpcontrol.logs;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import com.gmail.nowyarek.pvpcontrol.PVPControl;
import com.gmail.nowyarek.pvpcontrol.annotations.AsyncOnly;
import com.gmail.nowyarek.pvpcontrol.basic.FileManager;

public class LogsManager {
	private Plugin plugin;
	protected FileManager fileManager;
	protected final int maxlogSize;
	
	private Logger errorsLogger;
	
	public LogsManager(Plugin plugin, FileManager fileManager, int maxlogSize) {
		this.plugin = plugin;
		this.maxlogSize = maxlogSize;
		this.fileManager = fileManager;
	}
	
	@AsyncOnly
	public void logError(String msg) {
		if(Thread.currentThread().getId()==PVPControl.mainThreadID) {
			plugin.getServer().getScheduler().runTaskLaterAsynchronously(
					plugin, 
					() -> { errorsLogger.log(msg); } ,
					0
					);
		}else {
			errorsLogger.log(msg);
		}
	}
	
	//===========================================================================
	
	public void enable() {
		errorsLogger = new SimpleLogger(this, "error");
		errorsLogger.initialize();
	}
	
	public void disable() {
		errorsLogger.deinitialize();
	}
	
	protected int getLastLineNumber(FileConfiguration file) {
		int x=0, temp;
		for(String lineString : file.getKeys(false)) {
			if((temp = Integer.parseInt(lineString.split("|")[0]))>x) x = temp;
		}
		return x;
	}
	
}


/*try {
		throw new LogFileHandleException("Async Call - IO delays");
	} catch (LogFileHandleException e) {
		e.printStackTrace();
		return;
	}
			*/
