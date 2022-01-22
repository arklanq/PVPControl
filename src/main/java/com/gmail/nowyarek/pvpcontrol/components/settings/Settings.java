package com.gmail.nowyarek.pvpcontrol.components.settings;

import com.gmail.nowyarek.pvpcontrol.PVPControl;
import com.gmail.nowyarek.pvpcontrol.components.configuration.ConfigInitializationException;
import com.gmail.nowyarek.pvpcontrol.components.configuration.ConfigWithDefaults;
import com.gmail.nowyarek.pvpcontrol.components.configuration.ConfigurationValidationException;
import com.gmail.nowyarek.pvpcontrol.components.logging.PluginLogger;
import com.gmail.nowyarek.pvpcontrol.components.plugin.PluginEnableEvent;
import com.google.common.eventbus.Subscribe;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class Settings {
    private final ConfigWithDefaults config;
    private final PluginLogger logger;
    public GeneralSettings General;

    @Inject
    Settings(PVPControl plugin, PluginLogger logger) {
        this.config = new ConfigWithDefaults(plugin, logger, "settings.yml");
        this.logger = logger;
        plugin.getEventBus().register(this);
    }

    @Subscribe
    void onPluginEnable(PluginEnableEvent ignoredE) throws ConfigInitializationException {
        this.reload();
    }

    public void reload() throws ConfigInitializationException {
        try {
            this.config.initialize().get();
            this.initializeSections();
        } catch (Exception ex) {
            throw new ConfigInitializationException(this.config.fileName, ex);
        }
    }

    private void initializeSections() {
        this.General = new GeneralSettings(config);

        List<String> violations = Stream.of(this.General)
            .map((SettingsSection section) -> {
                try {
                    section.init();
                    return null;
                } catch (ConfigurationValidationException e) {
                    return e.getMessage();
                }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        if(violations.size() > 0)
            this.logger.warn(
                String.format(
                    "There was %s during validation of `settings.yml` configuration file.",
                    violations.size() == 1 ? "an issue" : "several issues"
                )
            );

        violations.forEach((violation) -> this.logger.warn(String.format("- %s", violation)));
    }

}
