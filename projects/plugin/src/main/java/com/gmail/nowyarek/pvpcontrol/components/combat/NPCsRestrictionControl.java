package com.gmail.nowyarek.pvpcontrol.components.combat;

import com.gmail.nowyarek.pvpcontrol.components.settings.SettingsLoadEvent;
import com.gmail.nowyarek.pvpcontrol.components.settings.SettingsProvider;
import com.google.common.eventbus.Subscribe;

import jakarta.inject.Inject;
import java.util.EventListener;

public class NPCsRestrictionControl implements CombatControl, EventListener {
    private final CombatEventSource combatEventSource;
    private final SettingsProvider settingsProvider;
    private volatile boolean isListenerRegistered = false;

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
        if (this.isListenerRegistered && this.settingsProvider.get().PvP().Damager().areNPCsPermitted())
            this.unregisterListener();
        else if (!this.isListenerRegistered && !this.settingsProvider.get().PvP().Damager().areNPCsPermitted())
            this.registerListener();
    }

    private void registerListener() {
        if(!this.isListenerRegistered) {
            this.combatEventSource.getEventBus().register(this);
            this.isListenerRegistered = true;
        }
    }

    private void unregisterListener() {
        if(this.isListenerRegistered) {
            this.combatEventSource.getEventBus().unregister(this);
            this.isListenerRegistered = false;
        }
    }

    @Subscribe
    void onSettingsReloadEvent(SettingsLoadEvent e) {
        this.determineListenerRegistrationState();
    }

    @Subscribe
    void onPlayerDamagePlayerEvent(PlayerDamagePlayerEvent e) {
        // Cancel the event if NPCs as damagers are not permitted
        if (e.getDamager().hasMetadata("NPC")) e.setCancelled(true);
    }
}
