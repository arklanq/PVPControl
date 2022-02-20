package com.gmail.nowyarek.pvpcontrol.components.configuration;

import com.google.inject.AbstractModule;

public class ConfigurationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ConfigFactory.class);
    }

}
