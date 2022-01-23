package com.gmail.nowyarek.pvpcontrol.components.configuration;

import com.gmail.nowyarek.pvpcontrol.PVPControl;
import com.gmail.nowyarek.pvpcontrol.components.logging.PluginLogger;

import javax.inject.Inject;
import javax.inject.Provider;

public class ConfigFactory {
    private final Provider<PVPControl> pluginProvider;
    private final Provider<PluginLogger> loggerProvider;

    @Inject
    public ConfigFactory(Provider<PVPControl> pluginProvider, Provider<PluginLogger> loggerProvider) {
        this.pluginProvider = pluginProvider;
        this.loggerProvider = loggerProvider;
    }

    public ConfigBase createConfigBase(String fileName) {
        return new ConfigBase(
            this.pluginProvider.get(),
            this.loggerProvider.get(),
            fileName
        );
    }

    public ConfigWithDefaults createConfigWithDefaults(String fileName) {
        return new ConfigWithDefaults(
            this.pluginProvider.get(),
            this.loggerProvider.get(),
            fileName
        );
    }

}
