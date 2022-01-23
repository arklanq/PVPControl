package com.gmail.nowyarek.pvpcontrol.components.settings;

import com.gmail.nowyarek.pvpcontrol.components.logging.PluginLogger;

import javax.inject.Inject;
import javax.inject.Provider;

public class ViolationsProcessorFactory {
    private final Provider<PluginLogger> loggerProvider;

    @Inject
    public ViolationsProcessorFactory(Provider<PluginLogger> loggerProvider) {
        this.loggerProvider = loggerProvider;
    }

    public ViolationsProcessor create(int configVersion, int defaultConfigVersion) {
        return new ViolationsProcessor(
            this.loggerProvider.get(),
            configVersion,
            defaultConfigVersion
        );
    }

}
