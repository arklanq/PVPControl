package com.gmail.nowyarek.pvpcontrol.components.combat;

import com.google.inject.AbstractModule;

public class CombatModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CombatDurationTimerRunnable.class);
        bind(CombatDurationTimer.class).asEagerSingleton();

        bind(EntityDamageByEntityListener.class).asEagerSingleton();
        bind(FlyRestrictionControl.class).asEagerSingleton();

        bind(EventsTester.class).asEagerSingleton();
    }
}
