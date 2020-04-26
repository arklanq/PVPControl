package com.gmail.nowyarek.pvpcontrol.configuration;

import com.gmail.nowyarek.pvpcontrol.PVPControl;
import com.gmail.nowyarek.pvpcontrol.io.Text;
import com.gmail.nowyarek.pvpcontrol.io.Variables;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;

public class TranslationsConfig extends ConfigWithDefaults {

    public TranslationsConfig(PVPControl plugin, String configName) {
        super(plugin, "language/" + configName);
    }

    @Override
    public void initialize() {
        try {
            super.initialize();
            if(oldConfiguration) {
                plugin.getConsole().warning(Text.MISSING_LANGUAGE_STATEMENTS, new Variables("%file%", configName));
            }
        } catch(InvalidConfigurationException e) {
            this.initializeDefaults();
            configuration = defaultsConfiguration;
            plugin.getConsole().error(Text.CORRUPTED_CONFIG, new Variables("%file%", configName));
        }
    }

    @NotNull
    public String getEntry(String key) {
        assert this.configuration != null;
        String value = this.configuration.getString(key);
        if(value == null) {
            throw new NullPointerException(String.format("Missing translation key: '%s'.", key));
        }
        return value.replaceAll("&", "\u00A7");
    }

}
