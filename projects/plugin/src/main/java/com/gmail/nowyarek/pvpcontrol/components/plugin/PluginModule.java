package com.gmail.nowyarek.pvpcontrol.components.plugin;

import com.gmail.nowyarek.pvpcontrol.PvPControlPlugin;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class PluginModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(PluginEnableEventListener.class);
        this.bind(PluginDisableEventListener.class);
        this.bind(PluginEventsExecutive.class);
    }

    @Provides
    public JavaPlugin provideJavaPlugin(PvPControlPlugin plugin) {
        return plugin;
    }

    @Provides
    @PluginVersion
    public String providePluginVersion(PvPControlPlugin plugin) {
        return plugin.getDescription().getVersion();
    }

    @Provides
    @PluginDataFolder
    public File providePluginDataFolder(PvPControlPlugin plugin) {
        return plugin.getDataFolder();
    }

}
