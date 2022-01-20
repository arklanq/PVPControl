package com.gmail.nowyarek.pvpcontrol.components.configuration;

import com.google.inject.AbstractModule;

public class ConfigurationModule extends AbstractModule {

    @Override
    public void configure() {
        bind(Settings.class);
        bind(SettingsInitializer.class).asEagerSingleton();
    }

}
