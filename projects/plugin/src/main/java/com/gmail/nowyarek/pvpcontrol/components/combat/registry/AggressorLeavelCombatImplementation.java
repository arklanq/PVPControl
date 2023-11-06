package com.gmail.nowyarek.pvpcontrol.components.combat.registry;

import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import jakarta.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class AggressorLeavelCombatImplementation {
    private final ConcurrentHashMap<Player, CombatInfo> combatInfoMap;
    private final Server server;

    public AggressorLeavelCombatImplementation(ConcurrentHashMap<Player, CombatInfo> combatInfoMap, Server server) {
        this.combatInfoMap = combatInfoMap;
        this.server = server;
    }

    CompletableFuture<Optional<CombatInfo>> tryKickAggressorFromCombat(Player victim, Entity aggressor) {
        return CompletableFuture.supplyAsync(
            () -> {
                @Nullable
                CombatInfo resultCombatInfo = this.combatInfoMap.computeIfPresent(victim, (Player _player, @Nullable CombatInfo prevCombatInfo) -> {
                    Map<Entity, Long> newAggressorsMap = new HashMap<>(prevCombatInfo.getAggressorsMap());
                    newAggressorsMap.remove(aggressor);

                    CombatInfo combatInfo = new CombatInfo(prevCombatInfo.getStartTimestamp(), prevCombatInfo.getEndTimestamp(), newAggressorsMap);

                    // Create an event object
                    AggressorLeaveCombatEvent e = new AggressorLeaveCombatEvent(victim, aggressor, combatInfo);
                    // Propagate the event accross plugin consumers
                    this.server.getPluginManager().callEvent(e);

                    // return newly created CombatInfo object (& update value in map).
                    return combatInfo;
                });

                return Optional.ofNullable(resultCombatInfo);
            }
        );
    }

    static class Factory {
        private final Server server;

        @Inject
        Factory(JavaPlugin plugin) {
            this.server = plugin.getServer();
        }

        AggressorLeavelCombatImplementation create(ConcurrentHashMap<Player, CombatInfo> combatInfoMap) {
            return new AggressorLeavelCombatImplementation(combatInfoMap, this.server);
        }

    }
}
