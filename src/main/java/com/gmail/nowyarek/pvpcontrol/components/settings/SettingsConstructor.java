package com.gmail.nowyarek.pvpcontrol.components.settings;

import com.gmail.nowyarek.pvpcontrol.PVPControl;
import com.gmail.nowyarek.pvpcontrol.components.configuration.ConfigFactory;
import com.gmail.nowyarek.pvpcontrol.components.configuration.ConfigInitializationException;
import com.gmail.nowyarek.pvpcontrol.components.configuration.ConfigWithDefaults;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class SettingsConstructor {
    private final ConfigWithDefaults config;
    private final ViolationsProcessorFactory violationsProcessorFactory;

    @Inject
    SettingsConstructor(PVPControl plugin, ConfigFactory configFactory, ViolationsProcessorFactory violationsProcessorFactory) {
        this.config = configFactory.createConfigWithDefaults("settings.yml");
        this.violationsProcessorFactory = violationsProcessorFactory;
        plugin.getEventBus().register(this);
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

        UnrecognizedOptionsProcessor unrecognizedOptionsProcessor = new UnrecognizedOptionsProcessor(this.config.configuration, this.config.defaultsConfiguration);
        violations.addAll(unrecognizedOptionsProcessor.process());

        ViolationsProcessor violationsProcessor = this.violationsProcessorFactory.create(
            settings.General().getConfigVersion(),
            this.config.defaultsConfiguration.getInt("General.configVersion")
        );
        violationsProcessor.process(violations);

        return settings;
    }
}
