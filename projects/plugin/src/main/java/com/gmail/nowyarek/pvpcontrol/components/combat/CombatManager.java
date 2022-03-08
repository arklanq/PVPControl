package com.gmail.nowyarek.pvpcontrol.components.combat;

import com.gmail.nowyarek.pvpcontrol.PvPControlPlugin;
import com.gmail.nowyarek.pvpcontrol.components.logging.PluginLogger;
import com.gmail.nowyarek.pvpcontrol.models.EventsSource;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Singleton
public class CombatManager implements EventsSource {
    private final Server server;
    private final PluginLogger logger;
    final CombatInfoRegistry combatInfoRegistry;
    final EventBus eventBus = new EventBus();

    @Inject
    CombatManager(PvPControlPlugin plugin, PluginLogger logger, CombatInfoRegistry combatInfoRegistry) {
        this.server = plugin.getServer();
        this.logger = logger;
        this.combatInfoRegistry = combatInfoRegistry;
    }

    public ImmutableList<Player> getAllPlayersDuringCombat() {
        return ImmutableList.copyOf(
            server.getOnlinePlayers()
                .stream()
                .filter(this::isPlayerDuringCombat)
                .collect(Collectors.toList())
        );
    }

    public boolean isPlayerDuringCombat(Player p) {
        return this.combatInfoRegistry.getCombatInfo(p).isPresent();
    }

    public CompletableFuture<Void> beginCombat(Player victim) {
        return this.beginCombat(victim, null);
    }

    public CompletableFuture<Void> beginCombat(Player victim, @Nullable Entity aggressor) {
        return CompletableFuture
            .runAsync(() -> {
                // Create an event object
                CombatStartEvent e = new CombatStartEvent(victim);
                // Propagate the event accross plugin consumers
                this.server.getPluginManager().callEvent(e);
                // If any of the plugin consumers cancels the event then exit immediately
                if (e.isCancelled()) return;

                if(aggressor != null) {
                    // If event has not been cancelled then add new aggressor
                    this.combatInfoRegistry.addAggressors(victim, new Entity[]{aggressor}).join();
                } else {
                    this.combatInfoRegistry.startCombat(victim).join();
                }

                // And propagate the same event through internal listeners to trigger visual effects
                this.eventBus.post(e);

                this.logger.debug("Combat started");
            });
    }

    public CompletableFuture<Void> endCombat(Player victim) {
        // End combat immediately. For now we don't if the combat is even taking place right now.
        return this.combatInfoRegistry.endCombat(victim)
            .thenAccept((Optional<CombatInfo> combatInfo) -> {
                /*
                 * Here we check if the the CombatInfo object is present.
                 * If yes - it means that the combat was actually taking place in the past, but was ended by us.
                 * If not - it means that the combat is not going on so we have nothing to stop.
                 */
                if (!combatInfo.isPresent()) return; // It means that the player even wasn not even during combat

                // Create an event object
                CombatEndEvent e = new CombatEndEvent(victim);
                // Propagate the event first through internal listeners to shut down any side effects
                this.eventBus.post(e);
                // .. then propagate the event accross plugin consumers
                this.server.getPluginManager().callEvent(e);

                this.logger.debug("Combat ended");
            });
    }

    /**
     * Available events:
     * <ul>
     *     <li>{@link CombatStartEvent}</li>
     *     <li>{@link CombatEndEvent}</li>
     * </ul>
     */
    @Override
    public EventBus getEventBus() {
        return eventBus;
    }

}
