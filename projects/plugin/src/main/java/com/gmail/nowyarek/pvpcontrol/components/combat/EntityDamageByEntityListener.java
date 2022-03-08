package com.gmail.nowyarek.pvpcontrol.components.combat;

import com.gmail.nowyarek.pvpcontrol.PvPControlPlugin;
import com.gmail.nowyarek.pvpcontrol.components.metadata.MetaData;
import com.gmail.nowyarek.pvpcontrol.components.permissions.Permission;
import com.gmail.nowyarek.pvpcontrol.components.plugin.PluginDisableEvent;
import com.gmail.nowyarek.pvpcontrol.components.plugin.PluginEnableEvent;
import com.gmail.nowyarek.pvpcontrol.components.settings.SettingsProvider;
import com.gmail.nowyarek.pvpcontrol.models.EventsSource;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Injector;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.EventListener;
import java.util.concurrent.CompletableFuture;

class EntityDamageByEntityListener implements Listener, EventListener, EventsSource {
    private final PvPControlPlugin plugin;
    private final SettingsProvider settingsProvider;
    private final CombatManager combatManager;
    private final EventBus eventBus = new EventBus();

    @Inject
    EntityDamageByEntityListener(
        PvPControlPlugin plugin, SettingsProvider settingsProvider, CombatManager combatManager, Injector guiceInjector
    ) {
        this.plugin = plugin;
        this.settingsProvider = settingsProvider;
        this.combatManager = combatManager;

        plugin.getEventBus().register(this);
    }

    @Subscribe
    void onPluginEnable(PluginEnableEvent e) {
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Subscribe
    void onPluginDisable(PluginDisableEvent e) {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    void onEntityDamageEvent(EntityDamageByEntityEvent e) {
        // Return if event is cancelled
        if (e.isCancelled()) return;
        // Return if the entity isn't a `Player`
        if (!(e.getEntity() instanceof Player)) return;

        // Cast object, already known to be of type `Player`
        Player victim = (Player) e.getEntity();
        Entity damagerEntity = e.getDamager();

        // Return if the victim entity is an NPC
        if (victim.hasMetadata("npc")) return;
        // Return if the player is attempting to deal damage to himself
        if(victim.getUniqueId().compareTo(damagerEntity.getUniqueId()) == 0) return;
        // Return if the victim player is a staff member under protection
        if(victim.hasPermission(Permission.Commands.TOGGLE.value()) && victim.hasMetadata(MetaData.Bypass.OP_PROTECTION.value())) return;

        // Determine the damager type (looking for type `Player`)
        @Nullable
        Player damagerPlayer = null;
        if (damagerEntity instanceof Player) {
            damagerPlayer = (Player) damagerEntity;
        } else if (damagerEntity instanceof Projectile) {
            Projectile projectile = (Projectile) damagerEntity;

            // Return if projectiles as damagers are not permitted
            if(!this.settingsProvider.get().PvP().Damager().areProjectilesPermitted()) return;
            // Return if the projectile shooter is not of type `Player`
            if (!(projectile.getShooter() instanceof Player)) return;

            damagerPlayer = (Player) projectile.getShooter();
        }

        // Return if the damager was not recognized to be of type `Player`
        if(damagerPlayer == null) return;
        // Return if NPCs as damagers are not permitted
        if(!this.settingsProvider.get().PvP().Damager().areNPCsPermitted() && damagerPlayer.hasMetadata("npc")) return;
        // Return if the damager player is a staff member under protection
        if(damagerPlayer.hasPermission(Permission.Commands.TOGGLE.value()) && victim.hasMetadata(MetaData.Bypass.OP_PROTECTION.value())) return;

        // Propagate `PlayerDamagePlayerEvent` event and return if has been cancelled
        Cancellable event = new PlayerDamagePlayerEvent(victim, damagerPlayer);
        this.eventBus.post(event);
        if(event.isCancelled()) return;

        CompletableFuture.allOf(
            this.combatManager.beginCombat(victim, damagerPlayer),
            this.combatManager.beginCombat(damagerPlayer, victim)
        ).join();
    }

    /**
     * Available events:
     * <ul>
     *     <li>{@link PlayerDamagePlayerEvent}</li>
     * </ul>
     */
    @Override
    public EventBus getEventBus() {
        return this.eventBus;
    }
}
