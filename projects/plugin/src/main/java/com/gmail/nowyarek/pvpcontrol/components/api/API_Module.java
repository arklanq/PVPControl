package com.gmail.nowyarek.pvpcontrol.components.api;

import com.google.inject.AbstractModule;

public class API_Module extends AbstractModule {

    @Override
    protected void configure() {
        bind(PvPControlImplementation.class).asEagerSingleton();
    }
}
