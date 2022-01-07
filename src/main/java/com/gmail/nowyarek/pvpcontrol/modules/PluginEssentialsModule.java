package com.gmail.nowyarek.pvpcontrol.modules;

import co.aikar.taskchain.TaskChainFactory;
import com.gmail.nowyarek.pvpcontrol.annotations.PluginLogger;
import com.gmail.nowyarek.pvpcontrol.providers.TaskChainFactoryProvider;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class PluginEssentialsModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TaskChainFactory.class).toProvider(TaskChainFactoryProvider.class).in(Scopes.SINGLETON);
    }

    @Provides
    @PluginLogger
    public Logger provideLogger(JavaPlugin plugin) {
        return plugin.getLogger();
    }

}
