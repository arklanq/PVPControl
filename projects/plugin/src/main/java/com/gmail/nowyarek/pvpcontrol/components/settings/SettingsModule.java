package com.gmail.nowyarek.pvpcontrol.components.settings;

import com.google.inject.AbstractModule;

public class SettingsModule extends AbstractModule {

    @Override
    public void configure() {
        bind(ViolationsProcessor.class);
        bind(SettingsConstructor.class);
        bind(SettingsProvider.class).asEagerSingleton();
        bind(Settings.class).toProvider(SettingsProvider.class);
    }

}