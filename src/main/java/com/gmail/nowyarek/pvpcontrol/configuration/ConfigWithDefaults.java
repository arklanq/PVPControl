package com.gmail.nowyarek.pvpcontrol.configuration;

import com.gmail.nowyarek.pvpcontrol.PVPControl;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigWithDefaults extends ConfigBase {
    public YamlConfiguration defaultsConfiguration;
    public boolean oldConfiguration = false;

    public ConfigWithDefaults(PVPControl plugin, String configName) {
        super(plugin, configName);
    }

    @Override
    public void initialize() {
        super.initialize();
        this.initializeDefaults();
    }

    protected void initializeDefaults() {
        defaultsConfiguration = ConfigurationUtils.loadDefaultConfiguration("/" + configName);
        oldConfiguration = ConfigurationUtils.ensureConfigurationCompleteness(configuration, defaultsConfiguration);
    }

}
