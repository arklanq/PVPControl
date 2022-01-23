package com.gmail.nowyarek.pvpcontrol.components.settings;

import com.google.inject.AbstractModule;

public class SettingsModule extends AbstractModule {

    @Override
    public void configure() {
        bind(ViolationsProcessorFactory.class);
        bind(Settings.class).asEagerSingleton();
    }

}
