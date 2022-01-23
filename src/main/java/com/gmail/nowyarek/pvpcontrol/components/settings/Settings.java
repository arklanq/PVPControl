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
    private final int CURRENT_VERSION = 1;
    private final ConfigWithDefaults config;
    private final PluginLogger logger;
    public GeneralSettings General;
    public PVPSettings PVP;

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
        this.PVP = new PVPSettings(config.configuration, config.defaultsConfiguration);

        List<String> violations = Stream.of(this.General, this.PVP)
            .map((SettingsSection section) -> section.init().getViolations())
            .flatMap(Collection::stream)
            .collect(Collectors.toList());

        if (this.General.getConfigVersion() != this.CURRENT_VERSION) {
            this.logger.warn("");
        }

        this.processViolations(violations);
    }

    private void processViolations(List<String> violations) {
        int MAX_VIOLATIONS_IN_CONSOLE = 10;

        if (this.General.getConfigVersion() != this.CURRENT_VERSION) {
            this.logger.warn(
                String.format(
                    "You are running PVPControl with an outdated config (vesion %s). " +
                        "Please follow the guidelines presented below to adjust your config to newest version (version %s).",
                    this.General.getConfigVersion(),
                    this.CURRENT_VERSION
                )
            );

            violations.add(0, String.format("Update `General.configVersion` to %s.", this.CURRENT_VERSION));
            MAX_VIOLATIONS_IN_CONSOLE++;
        } else if (violations.size() > 0)
            this.logger.warn(
                String.format(
                    "There was %s during validation of `settings.yml` configuration file.",
                    violations.size() == 1 ? "an issue" : "several issues"
                )
            );

        for (int i = 0; i < MAX_VIOLATIONS_IN_CONSOLE && i < violations.size(); i++) {
            String violation = violations.get(i);
            this.logger.warn(String.format("- %s", violation));
        }

        if (violations.size() > MAX_VIOLATIONS_IN_CONSOLE)
            this.logger.warn(String.format("... and %s more.", violations.size() - MAX_VIOLATIONS_IN_CONSOLE));
    }

}
