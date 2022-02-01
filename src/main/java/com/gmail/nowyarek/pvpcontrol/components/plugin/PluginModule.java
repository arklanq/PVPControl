package com.gmail.nowyarek.pvpcontrol.components.plugin;

import com.gmail.nowyarek.pvpcontrol.PVPControl;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class PluginModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(PluginEnabledEventListener.class);
        bind(PluginDisabledEventListener.class);
    }

    @Provides
    public JavaPlugin provideJavaPlugin(PVPControl plugin) {
        return plugin;
    }

    @Provides
    @PluginVersion
    public String providePluginVersion(PVPControl plugin) {
        return plugin.getDescription().getVersion();
    }

    @Provides
    @PluginDataFolder
    public File providePluginDataFolder(PVPControl plugin) {
        return plugin.getDataFolder();
    }

}
