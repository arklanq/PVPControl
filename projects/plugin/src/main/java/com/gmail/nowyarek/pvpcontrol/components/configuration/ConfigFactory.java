package com.gmail.nowyarek.pvpcontrol.components.configuration;

import com.gmail.nowyarek.pvpcontrol.PvPControlPlugin;
import com.gmail.nowyarek.pvpcontrol.components.logging.PluginLogger;
import com.gmail.nowyarek.pvpcontrol.components.plugin.PluginDataFolder;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.File;

public class ConfigFactory {
    private final Provider<PvPControlPlugin> pluginProvider;
    private final Provider<PluginLogger> loggerProvider;
    private final Provider<File> dataFolderProvider;

    @Inject
    public ConfigFactory(Provider<PvPControlPlugin> pluginProvider, Provider<PluginLogger> loggerProvider, @PluginDataFolder Provider<File> dataFolderProvider) {
        this.pluginProvider = pluginProvider;
        this.loggerProvider = loggerProvider;
        this.dataFolderProvider = dataFolderProvider;
    }

    public ConfigBase createConfigBase(String fileName) {
        return new ConfigBase(
            this.pluginProvider.get(),
            this.loggerProvider.get(),
            this.dataFolderProvider.get(),
            fileName
        );
    }

    public ConfigWithDefaults createConfigWithDefaults(String fileName) {
        return new ConfigWithDefaults(
            this.pluginProvider.get(),
            this.loggerProvider.get(),
            this.dataFolderProvider.get(),
            fileName
        );
    }

}
