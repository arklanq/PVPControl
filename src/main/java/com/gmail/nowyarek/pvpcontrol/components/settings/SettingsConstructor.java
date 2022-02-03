package com.gmail.nowyarek.pvpcontrol.components.settings;

import com.gmail.nowyarek.pvpcontrol.PVPControl;
import com.gmail.nowyarek.pvpcontrol.components.configuration.ConfigFactory;
import com.gmail.nowyarek.pvpcontrol.components.configuration.ConfigInitializationException;
import com.gmail.nowyarek.pvpcontrol.components.configuration.ConfigWithDefaults;
import com.gmail.nowyarek.pvpcontrol.components.logging.PluginLogger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class SettingsConstructor {
    private final PluginLogger logger;
    private final ConfigWithDefaults config;
    private final ViolationsProcessor violationsProcessor;

    @Inject
    SettingsConstructor(PVPControl plugin, PluginLogger logger, ConfigFactory configFactory, ViolationsProcessor violationsProcessor) {
        this.logger = logger;
        this.config = configFactory.createConfigWithDefaults("settings.yml");
        this.violationsProcessor = violationsProcessor;
    }

    CompletableFuture<Settings> construct() {
        return this.config.initialize()
            .thenApply(this::validateAndConstruct)
            .exceptionally((Throwable t) -> {
                throw new CompletionException(
                    new ConfigInitializationException(this.config.fileName, t)
                );
            });
    }

    private Settings validateAndConstruct(Void ignoredVoid) {
        Settings settings = new Settings(config.configuration, config.defaultsConfiguration);

        List<String> violations = new ArrayList<>(settings.init().getViolations());

        int configVersion = settings.General().getConfigVersion();
        int defaultConfigVersion = this.config.defaultsConfiguration.getInt("General.configVersion");

        if (configVersion != defaultConfigVersion) {
            this.logger.warn(
                String.format(
                    "You are running PVPControl with an outdated config (vesion %s). " +
                        "Please follow the guidelines presented below to adjust your config to newest version (version %s).",
                    configVersion,
                    defaultConfigVersion
                )
            );

            violations.add(0, String.format("Update `General.configVersion` to %s.", defaultConfigVersion));
        }

        UnrecognizedOptionsProcessor unrecognizedOptionsProcessor = new UnrecognizedOptionsProcessor(this.config.configuration, this.config.defaultsConfiguration);
        violations.addAll(unrecognizedOptionsProcessor.process());

        this.violationsProcessor.process(violations);

        return settings;
    }
}
