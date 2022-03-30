package com.gmail.nowyarek.pvpcontrol.components.combat.registry;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class CombatRegistryModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(StartCombatImplementation.Factory.class);
        bind(AggressorJoinCombatImplementation.Factory.class);
        bind(TriggerCombatImplementation.Factory.class);
        bind(AggressorLeavelCombatImplementation.Factory.class);
        bind(EndCombatImplementation.Factory.class);
        bind(CombatRegistry.class).in(Scopes.SINGLETON);
    }
}
