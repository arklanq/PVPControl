package com.gmail.nowyarek.pvpcontrol.components.injector;

import com.google.inject.AbstractModule;

public class InjectorConfigurationModule extends AbstractModule {

    @Override
    protected void configure() {
        /*
         * Prevent unexpected circular dependency chains
         * https://github.com/google/guice/wiki/CyclicDependencies
         */
        binder().disableCircularProxies();
        /*
         * Make it explicit when a constructor is intended for Guice to use
         * https://github.com/google/guice/wiki/MISSING_CONSTRUCTOR
         */
        binder().requireAtInjectOnConstructors();
        /*
         * Disable implicit bindings. Enforce that all bindings must be listed in a Module in order to be injected.
         * https://github.com/google/guice/wiki/JustInTimeBindings
         */
        binder().requireExplicitBindings();
    }

}
