package com.gmail.nowyarek.pvpcontrol.components.combat.registry;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import jakarta.inject.Inject;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class EndCombatImplementation {
    private final ConcurrentHashMap<Player, CombatInfo> combatInfoMap;
    private final Server server;

    public EndCombatImplementation(ConcurrentHashMap<Player, CombatInfo> combatInfoMap, Server server) {
        this.combatInfoMap = combatInfoMap;
        this.server = server;
    }

    CompletableFuture<Optional<CombatInfo>> tryEndCombat(Player victim) {
        return CompletableFuture.supplyAsync(
            () -> {
                AtomicReference<CombatInfo> combatInfoRef = new AtomicReference<>(null);

                this.combatInfoMap.computeIfPresent(victim, (Player _player, @Nullable CombatInfo prevCombatInfo) -> {
                    CombatInfo combatInfo = new CombatInfo(
                        prevCombatInfo.getStartTimestamp(), System.currentTimeMillis(), Collections.emptyMap()
                    );
                    combatInfoRef.set(combatInfo);

                    // Create an event object
                    CombatEndEvent e = new CombatEndEvent(victim, combatInfo);
                    // Propagate the event accross plugin consumers
                    this.server.getPluginManager().callEvent(e);

                    // return newly created CombatInfo object (& update value in map).
                    return null;
                });

                return Optional.of(combatInfoRef.get());
            }
        );
    }

    static class Factory {
        private final Server server;

        @Inject
        Factory(JavaPlugin plugin) {
            this.server = plugin.getServer();
        }

        EndCombatImplementation create(ConcurrentHashMap<Player, CombatInfo> combatInfoMap) {
            return new EndCombatImplementation(combatInfoMap, this.server);
        }

    }
}
