package com.gmail.nowyarek.pvpcontrol.components.combat;

import com.gmail.nowyarek.pvpcontrol.components.permissions.Permission;
import com.gmail.nowyarek.pvpcontrol.components.settings.SettingsLoadEvent;
import com.gmail.nowyarek.pvpcontrol.components.settings.SettingsProvider;
import com.google.common.eventbus.Subscribe;
import org.bukkit.entity.Player;

import jakarta.inject.Inject;
import java.util.EventListener;
import java.util.stream.Stream;

public class FlyRestrictionControl implements CombatControl, EventListener {
    private final CombatEventSource combatEventSource;
    private final SettingsProvider settingsProvider;
    private volatile boolean isListenerRegistered = false;

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
        if(!this.isListenerRegistered && this.settingsProvider.get().PvP().isFlyPermitted())
            this.registerListener();
        else if(this.isListenerRegistered && !this.settingsProvider.get().PvP().isFlyPermitted())
            this.unregisterListener();
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
        Stream.of(e.getVictim(), e.getDamager()).forEach((Player p) -> {
            if(!p.hasPermission(Permission.Bypass.FLY.value())) {
                if(p.isFlying()) p.setFlying(false);
                p.setAllowFlight(false);
            }
        });
    }
}
