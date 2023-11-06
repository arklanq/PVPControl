package com.gmail.nowyarek.pvpcontrol.components.combat;

import com.gmail.nowyarek.pvpcontrol.PvPControlPlugin;
import com.gmail.nowyarek.pvpcontrol.components.logging.PluginLogger;
import com.gmail.nowyarek.pvpcontrol.components.plugin.PluginDisableEvent;
import com.gmail.nowyarek.pvpcontrol.components.plugin.PluginEnableEvent;
import com.google.common.collect.ImmutableSet;
import com.google.common.eventbus.Subscribe;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.EventListener;

@Singleton
class CombatManager implements EventListener {
    private final PluginLogger logger;
    private final CombatDurationTimer combatDurationTimer;
    private final EntityDamageByEntityListener rootListener;
    private final ImmutableSet<CombatControl> combatControls;

    @Inject CombatManager(
        PvPControlPlugin plugin,
        PluginLogger pluginLogger,
        CombatDurationTimer combatDurationTimer,
        EntityDamageByEntityListener rootListener,
        FlyRestrictionControl flyRestrictionControl,
        NPCsRestrictionControl npcsRestrictionControl,
        AdminProtectionRestrictionControl adminProtectionRestrictionControl,
        InitialAdminProtectionControl initialAdminProtectionControl
    ) {
        this.logger = pluginLogger;
        this.combatDurationTimer = combatDurationTimer;
        this.rootListener = rootListener;

        this.combatControls = ImmutableSet.of(
            flyRestrictionControl,
            npcsRestrictionControl,
            adminProtectionRestrictionControl,
            initialAdminProtectionControl
        );

        plugin.getEventBus().register(this);
    }

    @Subscribe
    void onPluginEnable(PluginEnableEvent e) {
        this.logger.debug("Starting CombatDurationTimer...");
        this.combatDurationTimer.start();
        this.logger.debug(String.format("Integrating (%s) combat controls....", this.combatControls.size()));
        this.combatControls.forEach(CombatControl::integrate);
        this.logger.debug("Registering EntityDamageByEntityListener listener...");
        rootListener.register();
    }

    @Subscribe
    void onPluginDisable(PluginDisableEvent e) {
        this.logger.debug("Unregistering EntityDamageByEntityListener listener...");
        rootListener.unregister();
        this.logger.debug(String.format("Disintegrating (%s) combat controls....", this.combatControls.size()));
        this.combatControls.forEach(CombatControl::disintegrate);
        this.logger.debug("Stopping CombatDurationTimer...");
        this.combatDurationTimer.stop();
    }

}
