package com.gmail.nowyarek.pvpcontrol;
import com.gmail.nowyarek.pvpcontrol.commands.CommandsRegistry;
import com.gmail.nowyarek.pvpcontrol.configuration.ConfigurationManager;
import com.gmail.nowyarek.pvpcontrol.core.PvpModeHandler;
import com.gmail.nowyarek.pvpcontrol.core.PvpPlayersStore;
import com.gmail.nowyarek.pvpcontrol.io.Console;
import com.gmail.nowyarek.pvpcontrol.io.Localization;
import com.gmail.nowyarek.pvpcontrol.io.Prefixes;
import com.gmail.nowyarek.pvpcontrol.io.Text;
import com.gmail.nowyarek.pvpcontrol.listeners.ListenersManager;
import org.bukkit.plugin.java.JavaPlugin;


public class PVPControl extends JavaPlugin {
	private static long mainThreadID;
	private ConfigurationManager configurationManager;
	private Console console;
	private PvpPlayersStore playersStore;
	private CommandsRegistry cmdsRegistry;
	private PvpModeHandler pvpHandler;
	private ListenersManager listenersManager;

	@Override
	public void onEnable() {
		mainThreadID = Thread.currentThread().getId();
		try {
			console = new Console(this.getServer().getConsoleSender());
			console.lockBuffer();

			configurationManager = new ConfigurationManager(this);
			(new Localization()).provideTranslations(configurationManager.translationsConfig);
			(new Prefixes()).update();

			console.releaseBuffer();

			pvpHandler = new PvpModeHandler(this);
			pvpHandler.initialize();
			playersStore = new PvpPlayersStore(this);
			cmdsRegistry = new CommandsRegistry(this);
			cmdsRegistry.registerCommands();
			listenersManager = new ListenersManager(this);
			listenersManager.registerListeners();


			console.log(Text.PLUGIN_ENABLED);
		} catch(Exception e) {
			this.getServer().getConsoleSender().sendMessage(
					"\u00A74Sorry, there was a critical error in the plugin and will be disabled right now. "
					+ "Please make sure the plugin is up to date. If yes, contact me on Spigot @IdkMan."
			);
			e.printStackTrace();
			getServer().getPluginManager().disablePlugin(this);
		}
	}

	public void onReload() {
		console.lockBuffer();
		playersStore.lockBufferForAllPlayers();

		configurationManager.reload();
		(new Localization()).provideTranslations(configurationManager.translationsConfig);
		(new Prefixes()).update();
		pvpHandler.reinitialize();
		cmdsRegistry.reloadCommands();

		playersStore.releaseBufferForAllPlayers();
		console.releaseBuffer();

		pvpHandler.reinitialize();
	}
	
	@Override
	public void onDisable() {
		console.log(Text.PLUGIN_DISABLED);
		if(listenersManager != null) {
			listenersManager.unregisterListeners();
		}
		if(cmdsRegistry != null) {
			cmdsRegistry.unregisterCommands();
		}
		// All other event handlers will be unregistered automatically by spigot
	}

	public static boolean isMainThread() {
		return Thread.currentThread().getId() == PVPControl.mainThreadID;
	}
	public Console getConsole() {
		return console;
	}
	public ConfigurationManager getConfigurationManager() {
		return configurationManager;
	}
	public PvpModeHandler getPvpHandler() {
		return pvpHandler;
	}
	public PvpPlayersStore getPlayersStore() {
		return playersStore;
	}
}
