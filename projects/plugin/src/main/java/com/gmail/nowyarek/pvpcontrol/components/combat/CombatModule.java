package com.gmail.nowyarek.pvpcontrol.components.combat;

import com.google.inject.AbstractModule;

public class CombatModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CombatDurationTimerRunnable.class);
        bind(CombatDurationTimer.class);
        bind(CombatEventSource.class);
        bind(EntityDamageByEntityListener.class);
        bind(FlyRestrictionControl.class);
        bind(NPCsRestrictionControl.class);
        bind(AdminProtectionRestrictionControl.class);
        bind(InitialAdminProtectionControl.class);
        bind(CombatManager.class).asEagerSingleton();

        bind(EventsTester.class).asEagerSingleton();
    }
}
