package com.gmail.nowyarek.pvpcontrol.components.settings;

import com.gmail.nowyarek.pvpcontrol.components.logging.PluginLogger;

import javax.inject.Inject;
import javax.inject.Provider;

class ViolationsProcessorFactory {
    private final Provider<PluginLogger> loggerProvider;

    @Inject
    ViolationsProcessorFactory(Provider<PluginLogger> loggerProvider) {
        this.loggerProvider = loggerProvider;
    }

    ViolationsProcessor create(int configVersion, int defaultConfigVersion) {
        return new ViolationsProcessor(
            this.loggerProvider.get(),
            configVersion,
            defaultConfigVersion
        );
    }

}
