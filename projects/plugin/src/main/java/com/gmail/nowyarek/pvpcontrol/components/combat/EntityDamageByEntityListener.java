package com.gmail.nowyarek.pvpcontrol.components.combat;

import com.gmail.nowyarek.pvpcontrol.components.combat.registry.CombatRegistry;
import com.gmail.nowyarek.pvpcontrol.components.settings.SettingsProvider;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

class EntityDamageByEntityListener implements Listener {
    private final JavaPlugin plugin;
    private final SettingsProvider settingsProvider;
    private final CombatRegistry combatRegistry;
    private final CombatEventSource combatEventSource;

    @Inject
    EntityDamageByEntityListener(
        JavaPlugin plugin,
        SettingsProvider settingsProvider,
        CombatRegistry combatRegistry,
        CombatEventSource combatEventSource
    ) {
        this.plugin = plugin;
        this.settingsProvider = settingsProvider;
        this.combatRegistry = combatRegistry;
        this.combatEventSource = combatEventSource;
    }

    void register() {
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    void unregister() {
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
        if (victim.hasMetadata("NPC")) return;
        // Return if the player is attempting to deal damage to himself
        if(victim.getUniqueId().compareTo(damagerEntity.getUniqueId()) == 0) return;

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

        // Propagate `PlayerDamagePlayerEvent` event and return if has been cancelled
        Cancellable event = new PlayerDamagePlayerEvent(victim, damagerPlayer);
        this.combatEventSource.getEventBus().post(event);
        if(event.isCancelled()) return;

        CompletableFuture.allOf(
            this.combatRegistry.triggerCombat(victim, new Entity[] {damagerPlayer}),
            this.combatRegistry.triggerCombat(damagerPlayer, new Entity[] {victim})
        ).join();
    }

}
