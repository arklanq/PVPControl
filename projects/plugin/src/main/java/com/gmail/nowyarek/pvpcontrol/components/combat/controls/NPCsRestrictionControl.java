package com.gmail.nowyarek.pvpcontrol.components.combat.controls;

import com.gmail.nowyarek.pvpcontrol.components.combat.CombatEventSource;
import com.gmail.nowyarek.pvpcontrol.components.combat.PlayerDamagePlayerEvent;
import com.gmail.nowyarek.pvpcontrol.components.settings.SettingsLoadEvent;
import com.gmail.nowyarek.pvpcontrol.components.settings.SettingsProvider;
import com.google.common.eventbus.Subscribe;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.EventListener;
import java.util.concurrent.atomic.AtomicReference;

public class NPCsRestrictionControl implements CombatControl, EventListener {
    private final CombatEventSource combatEventSource;
    private final SettingsProvider settingsProvider;
    private final AtomicReference<AdminProtectionRestrictionControl.Listener> listenerRef = new AtomicReference<>(null);

    @Inject
    NPCsRestrictionControl(CombatEventSource combatEventSource, SettingsProvider settingsProvider) {
        this.combatEventSource = combatEventSource;
        this.settingsProvider = settingsProvider;
    }

    @Override
    public void integrate() {
        this.determineListenerRegistrationState();
        this.settingsProvider.getEventBus().register(this);
    }

    @Override
    public void disintegrate() {
        this.settingsProvider.getEventBus().unregister(this);
        this.unregisterListener();
    }

    private void determineListenerRegistrationState() {
        if (this.listener != null && this.settingsProvider.get().PvP().Damager().areNPCsPermitted())
            this.unregisterListener();
        else if (this.listener == null && !this.settingsProvider.get().PvP().Damager().areNPCsPermitted())
            this.registerListener();
    }

    private void registerListener() {
        if(this.listener == null) {
            this.listener = new Listener();
            this.combatEventSource.getEventBus().register(this.listener);
        }
    }

    private void unregisterListener() {
        if(this.listener != null) {
            this.combatEventSource.getEventBus().unregister(this.listener);
        }
    }

    @Subscribe
    void onSettingsReloadEvent(SettingsLoadEvent e) {
        this.determineListenerRegistrationState();
    }

    static class Listener implements EventListener {

        @Subscribe
        void onPlayerDamagePlayerEvent(PlayerDamagePlayerEvent e) {
            // Cancel the event if NPCs as damagers are not permitted
            if (e.getDamager().hasMetadata("NPC")) e.setCancelled(true);
        }

    }
}
