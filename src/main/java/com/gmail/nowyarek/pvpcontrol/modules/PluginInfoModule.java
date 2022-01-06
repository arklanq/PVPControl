package com.gmail.nowyarek.pvpcontrol.modules;

import com.gmail.nowyarek.pvpcontrol.PVPControl;
import com.gmail.nowyarek.pvpcontrol.annotations.PluginVersion;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginInfoModule extends AbstractModule {
    private final PVPControl plugin;

    public PluginInfoModule(PVPControl plugin) {
        this.plugin = plugin;
    }

    @Provides
    public PVPControl providePVPControlPlugin() {
        return this.plugin;
    }

    @Provides
    public JavaPlugin provideJavaPlugin() {
        return this.plugin;
    }

    @Provides
    @PluginVersion
    public String providePluginVersion() {
        return this.plugin.getDescription().getVersion();
    }

}
