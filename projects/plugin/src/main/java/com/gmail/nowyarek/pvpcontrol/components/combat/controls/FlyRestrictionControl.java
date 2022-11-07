package com.gmail.nowyarek.pvpcontrol.components.combat.controls;

import com.gmail.nowyarek.pvpcontrol.components.combat.CombatEventSource;
import com.gmail.nowyarek.pvpcontrol.components.combat.PlayerDamagePlayerEvent;
import com.gmail.nowyarek.pvpcontrol.components.permissions.Permission;
import com.gmail.nowyarek.pvpcontrol.components.settings.SettingsLoadEvent;
import com.gmail.nowyarek.pvpcontrol.components.settings.SettingsProvider;
import com.google.common.eventbus.Subscribe;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.EventListener;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class FlyRestrictionControl implements CombatControl, EventListener {
    private final CombatEventSource combatEventSource;
    private final SettingsProvider settingsProvider;
    private final AtomicReference<Listener> listenerRef = new AtomicReference<>(null);

    @Inject
    FlyRestrictionControl(CombatEventSource combatEventSource, SettingsProvider settingsProvider) {
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
        if(this.listener == null && this.settingsProvider.get().PvP().isFlyPermitted())
            this.registerListener();
        else if(this.listener != null && !this.settingsProvider.get().PvP().isFlyPermitted())
            this.unregisterListener();
    }

    private void registerListener() {
        synchronized (this.listenerRef) {
            @Nullable
            Listener listener = this.listenerRef.get();

            if(listener == null) {
                listener = new Listener();
                this.listenerRef.set(listener);
                this.combatEventSource.getEventBus().register(listener);
            }
        }
    }

    private void unregisterListener() {
        synchronized (this.listenerRef) {
            @Nullable
            Listener listener = this.listenerRef.get();

            if(listener != null) {
                this.combatEventSource.getEventBus().unregister(listener);
                this.listenerRef.set(null);
            }
        }
    }

    @Subscribe
    void onSettingsReloadEvent(SettingsLoadEvent e) {
        this.determineListenerRegistrationState();
    }

    static class Listener implements EventListener {

        @Subscribe
        void onPlayerDamagePlayerEvent(PlayerDamagePlayerEvent e) {
            Stream.of(e.getVictim(), e.getDamager()).forEach((Player p) -> {
                if(!p.hasPermission(Permission.Bypass.FLY.value())) {
                    if(p.isFlying()) p.setFlying(false);
                    p.setAllowFlight(false);
                }
            });
        }

    }
}
