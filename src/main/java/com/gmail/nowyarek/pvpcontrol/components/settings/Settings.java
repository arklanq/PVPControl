package com.gmail.nowyarek.pvpcontrol.components.settings;

import com.gmail.nowyarek.pvpcontrol.PVPControl;
import com.gmail.nowyarek.pvpcontrol.components.configuration.ConfigInitializationException;
import com.gmail.nowyarek.pvpcontrol.components.configuration.ConfigWithDefaults;
import com.gmail.nowyarek.pvpcontrol.components.logging.PluginLogger;
import com.gmail.nowyarek.pvpcontrol.components.plugin.PluginEnableEvent;
import com.google.common.eventbus.Subscribe;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.List;
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
        this.General = new GeneralSettings(config.configuration, config.defaultsConfiguration);

        List<String> violations = Stream.of(this.General)
            .map((SettingsSection section) -> section.init().getViolations())
            .flatMap(Collection::stream)
            .collect(Collectors.toList());

        this.processViolations(violations);
    }

    private void processViolations(List<String> violations) {
        if (violations.size() > 0)
            this.logger.warn(
                String.format(
                    "There was %s during validation of `settings.yml` configuration file.",
                    violations.size() == 1 ? "an issue" : "several issues"
                )
            );

        for (int i = 0; i < 3 && i < violations.size(); i++) {
            String violation = violations.get(i);
            this.logger.warn(String.format("- %s", violation));
        }

        if (violations.size() > 3)
            this.logger.warn(String.format("... and %s more.", violations.size() - 3));
    }

}
