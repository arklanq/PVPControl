package com.gmail.nowyarek.pvpcontrol.modules;

import com.gmail.nowyarek.pvpcontrol.PVPControl;
import com.gmail.nowyarek.pvpcontrol.annotations.PluginVersion;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginInfoModule extends AbstractModule {

    @Provides
    public JavaPlugin provideJavaPlugin(PVPControl plugin) {
        return plugin;
    }

    @Provides
    @PluginVersion
    public String providePluginVersion(PVPControl plugin) {
        return plugin.getDescription().getVersion();
    }

}
