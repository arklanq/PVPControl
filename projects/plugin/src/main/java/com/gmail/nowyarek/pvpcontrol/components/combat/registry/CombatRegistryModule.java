package com.gmail.nowyarek.pvpcontrol.components.combat.registry;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class CombatRegistryModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CombatInfoMap.class);
        bind(StartCombatImplementation.class);
        bind(AggressorJoinCombatImplementation.class);
        bind(TriggerCombatImplementation.class);
        bind(AggressorLeavelCombatImplementation.class);
        bind(EndCombatImplementation.class);
        bind(CombatRegistry.class).in(Scopes.SINGLETON);
    }
}
