package com.gmail.nowyarek.pvpcontrol.components.settings;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.OptionalBinder;

public class SettingsModule extends AbstractModule {

    @Override
    public void configure() {
        bind(ViolationsProcessorFactory.class);
        bind(SettingsConstructor.class);
        bind(SettingsProvider.class).asEagerSingleton();
        OptionalBinder.newOptionalBinder(binder(), Settings.class).setBinding().toProvider(SettingsProvider.class);
    }

}
