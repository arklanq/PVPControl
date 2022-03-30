package com.gmail.nowyarek.pvpcontrol.components.combat;

import com.gmail.nowyarek.pvpcontrol.PvPControlPlugin;
import com.gmail.nowyarek.pvpcontrol.components.metadata.MetaData;
import com.gmail.nowyarek.pvpcontrol.components.permissions.Permission;
import com.gmail.nowyarek.pvpcontrol.components.settings.SettingsLoadEvent;
import com.gmail.nowyarek.pvpcontrol.components.settings.SettingsProvider;
import com.google.common.eventbus.Subscribe;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import java.util.EventListener;

public class InitialAdminProtectionControl implements CombatControl, EventListener, Listener {
    private final SettingsProvider settingsProvider;
    private final JavaPlugin plugin;
    private boolean isListenerRegistered = false;

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
        if (this.settingsProvider.get().Features().AdminProtection().isEnabledByDefault() && !this.isListenerRegistered)
            this.registerListener();
        else if (!this.settingsProvider.get().Features().AdminProtection().isEnabledByDefault() && this.isListenerRegistered)
            this.unregisterListener();
    }

    private void registerListener() {
        if (!this.isListenerRegistered) {
            this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
            this.isListenerRegistered = true;
        }
    }

    private void unregisterListener() {
        if (this.isListenerRegistered) {
            HandlerList.unregisterAll(this);
            this.isListenerRegistered = false;
        }
    }

    @EventHandler
    void onPlayerJoin(PlayerJoinEvent e) {
        if (e.getPlayer().hasPermission(Permission.Commands.TOGGLE.value()) && !e.getPlayer().hasMetadata(MetaData.Bypass.OP_PROTECTION.value()))
            e.getPlayer().setMetadata(MetaData.Bypass.OP_PROTECTION.value(), new FixedMetadataValue(this.plugin, true));
        else if (!e.getPlayer().hasPermission(Permission.Commands.TOGGLE.value()) && e.getPlayer().hasMetadata(MetaData.Bypass.OP_PROTECTION.value()))
            e.getPlayer().removeMetadata(MetaData.Bypass.OP_PROTECTION.value(), this.plugin);
    }

}
