package com.gmail.nowyarek.pvpcontrol.logs;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.gmail.nowyarek.pvpcontrol.basic.Msg;
import com.gmail.nowyarek.pvpcontrol.exceptions.LogFileHandleException;

public class SimpleLogger implements Logger {
	private String logName;;
	private File file;
	private FileConfiguration fileConf;
	private int lastLine;
	private LogsManager logsManager;
	
	public SimpleLogger(LogsManager logsManager, String logName) {
		this.logsManager = logsManager;
		this.logName = logName;
	}
	
	@Override
	public String getLogName() {
		return logName;
	}

	@Override
	public File getFile() {
		return file;
	}

	@Override
	public FileConfiguration getFileConfiguration() {
		return fileConf;
	}

	@Override
	public int getLastLine() {
		return lastLine;
	}

	@Override
	public void log(String msg) {
		if(fileConf==null) throw new LogFileHandleException("ErrorLogger fileConf null");
		lastLine++;
		if(lastLine > logsManager.maxlogSize) {
			try {
				fileConf.save(logsManager.fileManager.getCurrentLogFile(logName));
				fileConf = YamlConfiguration.loadConfiguration(logsManager.fileManager.getNextLogFile(logName));
			} catch (Exception e) {
				e.printStackTrace();
				throw new LogFileHandleException("Problem ze stworzeniem nowego logu");
			}
		}
		try {
			fileConf.set(lastLine+"|"+(new SimpleDateFormat("yy-MM-dd hh:mm:ss").format(new Date()))+">> ", msg);
		} catch(Exception e) {
			e.printStackTrace();
			Msg.consoleLog("§4Problem z zapisaniem informacji w logu");
			return;
		}
	}

	@Override
	public void initialize() {
		fileConf = YamlConfiguration.loadConfiguration(logsManager.fileManager.getCurrentLogFile(logName));
		this.lastLine = logsManager.getLastLineNumber(fileConf);
	}

	@Override
	public void deinitialize() {
		try {
			fileConf.save(logsManager.fileManager.getCurrentLogFile(logName));
		} catch(IOException e) {
			throw new LogFileHandleException("deinitializeErrorsLog() #IOException");
		}
	}

}
