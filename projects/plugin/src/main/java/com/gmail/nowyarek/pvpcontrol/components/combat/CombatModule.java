package com.gmail.nowyarek.pvpcontrol.components.combat;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class CombatModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CombatInfoRegistry.class).in(Scopes.SINGLETON);
        bind(CombatManager.class).in(Scopes.SINGLETON);
        bind(CombatDurationTimerRunnable.class);
        bind(CombatDurationTimer.class).asEagerSingleton();

        bind(EntityDamageByEntityListener.class).asEagerSingleton();
        bind(FlyRestrictionControl.class).asEagerSingleton();
    }
}
