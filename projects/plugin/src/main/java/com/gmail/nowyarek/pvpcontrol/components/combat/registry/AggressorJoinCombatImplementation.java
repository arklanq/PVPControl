package com.gmail.nowyarek.pvpcontrol.components.combat.registry;

import com.gmail.nowyarek.pvpcontrol.components.settings.SettingsProvider;
import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

class AggressorJoinCombatImplementation {
    private final ConcurrentHashMap<Player, CombatInfo> combatInfoMap;
    private final SettingsProvider settingsProvider;
    private final Server server;

    public AggressorJoinCombatImplementation(ConcurrentHashMap<Player, CombatInfo> combatInfoMap, SettingsProvider settingsProvider, Server server) {
        this.combatInfoMap = combatInfoMap;
        this.settingsProvider = settingsProvider;
        this.server = server;
    }

    CompletableFuture<Optional<CombatInfo>> tryJoinAggressorToCombat(Player victim, Entity aggressor) {
        return CompletableFuture.supplyAsync(
            () -> {
                AtomicBoolean isCombatInfoUpdated = new AtomicBoolean(false);

                CombatInfo resultCombatInfo = this.combatInfoMap.computeIfPresent(victim, (Player _player, @Nullable CombatInfo prevCombatInfo) -> {
                    long endTimestamp = System.currentTimeMillis() + this.getCombatDurationMillis();

                    Map<Entity, Long> newAggressorsMap = new HashMap<>(prevCombatInfo.getAggressorsMap());
                    newAggressorsMap.put(aggressor, endTimestamp);

                    CombatInfo combatInfo = new CombatInfo(prevCombatInfo.getStartTimestamp(), endTimestamp, newAggressorsMap);

                    // Create an event object
                    AggressorJoinCombatEvent e = new AggressorJoinCombatEvent(victim, aggressor, combatInfo);
                    // Propagate the event accross plugin consumers
                    this.server.getPluginManager().callEvent(e);
                    // If any of the plugin consumers cancels the event then immediately return with previous CombatInfo object (do not map new value).
                    if (e.isCancelled()) return prevCombatInfo;

                    isCombatInfoUpdated.set(true);
                    // return newly created CombatInfo object (& update value in map).
                    return combatInfo;
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

        AggressorJoinCombatImplementation create(ConcurrentHashMap<Player, CombatInfo> combatInfoMap) {
            return new AggressorJoinCombatImplementation(combatInfoMap, this.settingsProvider, this.server);
        }

    }

}
