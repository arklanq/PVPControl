package com.gmail.nowyarek.pvpcontrol.components.resources;

import com.google.inject.AbstractModule;

public class ResourcesModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ResourceBundleUTF8Control.class);
    }

}
