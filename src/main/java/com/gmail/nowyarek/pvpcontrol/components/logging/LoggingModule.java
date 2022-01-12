package com.gmail.nowyarek.pvpcontrol.components.logging;

import com.google.inject.AbstractModule;

public class LoggingModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(DefaultLogLevelDetector.class);
        bind(LoggingConfiguration.class).asEagerSingleton();
        bind(PluginLogger.class);
    }
}
