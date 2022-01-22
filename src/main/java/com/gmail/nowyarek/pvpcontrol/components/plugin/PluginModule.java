package com.gmail.nowyarek.pvpcontrol.components.plugin;

import com.gmail.nowyarek.pvpcontrol.PVPControl;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(PluginEnableEventListener.class);
        bind(PluginDisableEventListener.class);
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

}
