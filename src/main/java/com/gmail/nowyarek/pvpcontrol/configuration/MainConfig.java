package com.gmail.nowyarek.pvpcontrol.configuration;

import com.gmail.nowyarek.pvpcontrol.PVPControl;
import com.gmail.nowyarek.pvpcontrol.io.Text;
import com.gmail.nowyarek.pvpcontrol.io.Variables;

public class MainConfig extends ConfigWithDefaults {

    public MainConfig(PVPControl plugin) {
        super(plugin, "config.yml");
    }

    @Override
    public void initialize() {
        super.initialize();
        if(oldConfiguration) {
            plugin.getConsole().warning(Text.OLD_CONFIG, new Variables("%file%", configName));
        }
    }
}
