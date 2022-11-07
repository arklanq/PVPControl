package com.gmail.nowyarek.pvpcontrol.components.combat.controls;

import com.gmail.nowyarek.pvpcontrol.PvPControlPlugin;
import com.gmail.nowyarek.pvpcontrol.components.metadata.MetaData;
import com.gmail.nowyarek.pvpcontrol.components.permissions.Permission;
import com.gmail.nowyarek.pvpcontrol.components.settings.SettingsLoadEvent;
import com.gmail.nowyarek.pvpcontrol.components.settings.SettingsProvider;
import com.google.common.eventbus.Subscribe;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.EventListener;
import java.util.concurrent.atomic.AtomicReference;

public class InitialAdminProtectionControl implements CombatControl, EventListener {
    private final SettingsProvider settingsProvider;
    private final JavaPlugin plugin;
    private final AtomicReference<AdminProtectionRestrictionControl.Listener> listenerRef = new AtomicReference<>(null);

    @Inject
    InitialAdminProtectionControl(SettingsProvider settingsProvider, PvPControlPlugin plugin) {
        this.settingsProvider = settingsProvider;
        this.plugin = plugin;
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

    @Subscribe
    void onSettingsReload(SettingsLoadEvent e) {
        this.determineListenerRegistrationState();
    }

    private void determineListenerRegistrationState() {
        if (this.listener == null &&  this.settingsProvider.get().Features().AdminProtection().isEnabledByDefault())
            this.registerListener();
        else if (this.listener != null && !this.settingsProvider.get().Features().AdminProtection().isEnabledByDefault())
            this.unregisterListener();
    }

    private void registerListener() {
        if (this.listener == null) {
            this.listener = new Listener(this.plugin);
            this.plugin.getServer().getPluginManager().registerEvents(this.listener, this.plugin);
        }
    }

    private void unregisterListener() {
        if (this.listener != null) {
            HandlerList.unregisterAll(this.listener);
        }
    }

    static class Listener implements org.bukkit.event.Listener {
        private final JavaPlugin plugin;

        Listener(JavaPlugin plugin) {
            this.plugin = plugin;
        }

        @EventHandler
        void onPlayerJoin(PlayerJoinEvent e) {
            if (e.getPlayer().hasPermission(Permission.Commands.TOGGLE.value()) && !e.getPlayer().hasMetadata(MetaData.Bypass.OP_PROTECTION.value()))
                e.getPlayer().setMetadata(MetaData.Bypass.OP_PROTECTION.value(), new FixedMetadataValue(this.plugin, true));
            else if (!e.getPlayer().hasPermission(Permission.Commands.TOGGLE.value()) && e.getPlayer().hasMetadata(MetaData.Bypass.OP_PROTECTION.value()))
                e.getPlayer().removeMetadata(MetaData.Bypass.OP_PROTECTION.value(), this.plugin);
        }

    }

}
