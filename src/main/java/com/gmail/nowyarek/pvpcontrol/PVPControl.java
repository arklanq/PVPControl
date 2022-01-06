package com.gmail.nowyarek.pvpcontrol;

import co.aikar.taskchain.TaskChainFactory;
import com.gmail.nowyarek.pvpcontrol.annotations.PluginVersion;
import com.gmail.nowyarek.pvpcontrol.modules.InjectorConfigurationModule;
import com.gmail.nowyarek.pvpcontrol.modules.PluginInfoModule;
import com.gmail.nowyarek.pvpcontrol.modules.TaskChainsConfigurationModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;

public class PVPControl extends JavaPlugin {
    private Injector guice;

    @Override
    public void onEnable() {
        this.guice = Guice.createInjector(
            new InjectorConfigurationModule(),
            new PluginInfoModule(this),
            new TaskChainsConfigurationModule()
        );

        //TODO: Move to `plugin-enable` listener
        String pluginVersion = this.guice.getInstance(Key.get(String.class, PluginVersion.class));
        this.getServer().getConsoleSender().sendMessage(
            String.format("§2v%s Enabled.", pluginVersion)
        );
    }

    @Override
    public void onDisable() {
        //TODO: Move to `plugin-disable` listener
        if(this.guice != null) {
            this.guice.getInstance(TaskChainFactory.class).shutdown(2500, TimeUnit.MILLISECONDS);
        }

        this.getServer().getConsoleSender().sendMessage("§7Disabled.");
    }

}