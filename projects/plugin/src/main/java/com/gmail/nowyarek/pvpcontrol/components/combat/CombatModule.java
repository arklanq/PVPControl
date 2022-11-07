package com.gmail.nowyarek.pvpcontrol.components.combat;

import com.gmail.nowyarek.pvpcontrol.components.combat.controls.AdminProtectionRestrictionControl;
import com.gmail.nowyarek.pvpcontrol.components.combat.controls.FlyRestrictionControl;
import com.gmail.nowyarek.pvpcontrol.components.combat.controls.InitialAdminProtectionControl;
import com.gmail.nowyarek.pvpcontrol.components.combat.controls.NPCsRestrictionControl;
import com.gmail.nowyarek.pvpcontrol.components.combat.registry.CombatRegistryModule;
import com.google.inject.AbstractModule;

public class CombatModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CombatEventSource.class);

        install(new CombatRegistryModule());

        bind(CombatDurationTimerRunnable.class);
        bind(CombatDurationTimer.class);
        bind(EntityDamageByEntityListener.class);
        bind(FlyRestrictionControl.class);
        bind(NPCsRestrictionControl.class);
        bind(AdminProtectionRestrictionControl.class);
        bind(InitialAdminProtectionControl.class);
        bind(CombatManager.class).asEagerSingleton();

        bind(EventsTester.class).asEagerSingleton();
    }
}
