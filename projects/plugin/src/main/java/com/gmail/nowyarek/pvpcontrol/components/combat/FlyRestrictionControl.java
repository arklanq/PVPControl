package com.gmail.nowyarek.pvpcontrol.components.combat;

import com.gmail.nowyarek.pvpcontrol.components.permissions.Permission;
import com.gmail.nowyarek.pvpcontrol.components.settings.SettingsLoadEvent;
import com.gmail.nowyarek.pvpcontrol.components.settings.SettingsProvider;
import com.google.common.eventbus.Subscribe;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.EventListener;
import java.util.stream.Stream;

public class FlyRestrictionControl implements EventListener {
    private final EntityDamageByEntityListener rootListener;
    private final SettingsProvider settingsProvider;
    private volatile boolean isListenerRegistered = false;

    @Inject
    FlyRestrictionControl(EntityDamageByEntityListener rootListener, SettingsProvider settingsProvider) {
        this.rootListener = rootListener;
        this.settingsProvider = settingsProvider;
        this.settingsProvider.getEventBus().register(this);
        this.determineListenerRegistrationState();
    }

    private void determineListenerRegistrationState() {
        if(this.isListenerRegistered && !this.settingsProvider.get().PvP().isFlyPermitted())
            this.unRegisterListener();
        else if(!this.isListenerRegistered && this.settingsProvider.get().PvP().isFlyPermitted())
            this.registerListener();
    }

    private void registerListener() {
        this.rootListener.getEventBus().register(this);
        this.isListenerRegistered = true;
    }

    private void unRegisterListener() {
        this.rootListener.getEventBus().unregister(this);
        this.isListenerRegistered = false;
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
