package com.gmail.nowyarek.pvpcontrol.components.plugin;

import com.google.inject.AbstractModule;

public class PluginEssentialsModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(PluginEnableEventListener.class);
        bind(PluginDisableEventListener.class);
    }

}
