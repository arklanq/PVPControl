package com.gmail.nowyarek.pvpcontrol.components.combat.effects;

import com.gmail.nowyarek.pvpcontrol.components.combat.CombatEventSource;
import com.gmail.nowyarek.pvpcontrol.components.combat.registry.CombatEndEvent;
import com.gmail.nowyarek.pvpcontrol.components.combat.registry.CombatStartEvent;
import com.gmail.nowyarek.pvpcontrol.components.settings.SettingsLoadEvent;
import com.gmail.nowyarek.pvpcontrol.components.settings.SettingsProvider;
import com.google.common.eventbus.Subscribe;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.EventListener;

public class BossbarEffect implements CombatEffect, EventListener {
    private final CombatEventSource combatEventSource;
    private final SettingsProvider settingsProvider;
    @Nullable
    private volatile Listener listener = null;

    @Inject
    BossbarEffect(CombatEventSource combatEventSource, SettingsProvider settingsProvider) {
        this.combatEventSource = combatEventSource;
        this.settingsProvider = settingsProvider;
    }

    @Override
    public void integrate() {

    }

    @Override
    public void disintegrate() {

    }

    private void determineListenerRegistrationState() {
        if(this.listener == null && this.settingsProvider.get().Features().Bossbar().isEnabled())
            this.registerListener();
        else if(this.listener != null && !this.settingsProvider.get().Features().Bossbar().isEnabled())
            this.unregisterListener();
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
            this.listener = null;
        }
    }

    @Subscribe
    void onSettingsReloadEvent(SettingsLoadEvent e) {
        this.determineListenerRegistrationState();
    }

    static class Listener {
        @Subscribe
        void onCombatStartEvent(CombatStartEvent e) {

        }

        @Subscribe
        void onCombatUpdateEvent(CombatStartEvent e) {

        }

        @Subscribe
        void onCombatEndEvent(CombatEndEvent e) {

        }
    }

}
