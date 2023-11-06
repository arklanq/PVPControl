package com.gmail.nowyarek.pvpcontrol.components.combat.registry;

import com.gmail.nowyarek.pvpcontrol.components.settings.SettingsProvider;
import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import jakarta.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

class TriggerCombatImplementation {
    private final ConcurrentHashMap<Player, CombatInfo> combatInfoMap;
    private final SettingsProvider settingsProvider;
    private final Server server;

    public TriggerCombatImplementation(ConcurrentHashMap<Player, CombatInfo> combatInfoMap, SettingsProvider settingsProvider, Server server) {
        this.combatInfoMap = combatInfoMap;
        this.settingsProvider = settingsProvider;
        this.server = server;
    }

    CompletableFuture<Optional<CombatInfo>> triggerCombat(Player victim, Entity[] aggressors) {
        return CompletableFuture.supplyAsync(
            () -> {
                AtomicBoolean isCombatInfoUpdated = new AtomicBoolean(false);

                CombatInfo resultCombatInfo = this.combatInfoMap.compute(victim, (Player _player, @Nullable CombatInfo previousCombatInfo) -> {
                    // Calculate combat end timestamp
                    long endTimestamp = System.currentTimeMillis() + this.getCombatDurationMillis();

                    if(previousCombatInfo == null) {
                        long combatStartTimestamp = System.currentTimeMillis();

                        // Define new aggressors map
                        Map<Entity, Long> newAggressorsMap = new HashMap<>();

                        // Filter out aggressors by propagating cancellable AggressorJoinCombatEvent
                        for(Entity aggressor : aggressors) {
                            CombatInfo combatInfo = new CombatInfo(
                                combatStartTimestamp,
                                endTimestamp,
                                new HashMap<Entity, Long>(newAggressorsMap) {{ put(aggressor, endTimestamp); }}
                            );

                            // Create an event object
                            AggressorJoinCombatEvent e = new AggressorJoinCombatEvent(victim, aggressor, combatInfo);
                            // Propagate the event accross plugin consumers
                            this.server.getPluginManager().callEvent(e);

                            // If any of the plugin consumers cancels the event then immediately return with previous CombatInfo object (do not map new value).
                            if (e.isCancelled()) continue;

                            // Put entry into the map
                            newAggressorsMap.put(aggressor, endTimestamp);
                        }

                        if(newAggressorsMap.size() == 0)
                            return null; // don't update map if no aggressor has joined (combat without aggressors)

                        // Create new CombatInfo object
                        CombatInfo combatInfo = new CombatInfo(System.currentTimeMillis(), endTimestamp, newAggressorsMap);

                        // Create an event object
                        CombatStartEvent e = new CombatStartEvent(victim, combatInfo);
                        // Propagate the event accross plugin consumers
                        this.server.getPluginManager().callEvent(e);
                        // If any of the plugin consumers cancels the event then immediately return with null (do not map key).
                        if(e.isCancelled()) return null;

                        // Set `map-updated` flag to true
                        isCombatInfoUpdated.set(true);
                        // return newly created CombatInfo object (& update value in map).
                        return combatInfo;
                    } else {
                        Map<Entity, Long> previousAggressorsMap = previousCombatInfo.getAggressorsMap();
                        // Define new aggressors map
                        Map<Entity, Long> newAggressorsMap = new HashMap<>();

                        // Filter out aggressors by propagating cancellable AggressorJoinCombatEvent
                        for(Entity aggressor : aggressors) {
                            if(!previousAggressorsMap.containsKey(aggressor)) {
                                CombatInfo combatInfo = new CombatInfo(
                                    previousCombatInfo.getStartTimestamp(),
                                    endTimestamp,
                                    new HashMap<Entity, Long>(newAggressorsMap) {{ put(aggressor, endTimestamp); }}
                                );

                                // Create an event object
                                AggressorJoinCombatEvent e = new AggressorJoinCombatEvent(victim, aggressor, combatInfo);
                                // Propagate the event accross plugin consumers
                                this.server.getPluginManager().callEvent(e);

                                // If any of the plugin consumers cancels the event then immediately return with previous CombatInfo object (do not map new value).
                                if (e.isCancelled()) continue;

                                // Put entry into the map
                                newAggressorsMap.put(aggressor, endTimestamp);
                            } else {
                                // Update existing entry in map
                                newAggressorsMap.put(aggressor, endTimestamp);
                            }
                        }

                        if(newAggressorsMap.size() == 0)
                            return previousCombatInfo; // don't update map if no aggressor has joined

                        // Set `map-updated` flag to true
                        isCombatInfoUpdated.set(true);
                        // return newly created CombatInfo object (& update value in map).
                        return new CombatInfo(previousCombatInfo.getStartTimestamp(), endTimestamp, newAggressorsMap);
                    }
                });

                if(isCombatInfoUpdated.get())
                    return Optional.of(Objects.requireNonNull(resultCombatInfo));
                else
                    return Optional.empty();
            }
        );
    }

    private long getCombatDurationMillis() {
        return this.settingsProvider.get().PvP().getCombatDuration() * 1000L;
    }

    static class Factory {
        private final SettingsProvider settingsProvider;
        private final Server server;

        @Inject
        Factory(SettingsProvider settingsProvider, JavaPlugin plugin) {
            this.settingsProvider = settingsProvider;
            this.server = plugin.getServer();
        }

        TriggerCombatImplementation create(ConcurrentHashMap<Player, CombatInfo> combatInfoMap) {
            return new TriggerCombatImplementation(combatInfoMap, this.settingsProvider, this.server);
        }

    }

}
