package com.gmail.nowyarek.pvpcontrol.components.combat.registry;

import com.gmail.nowyarek.pvpcontrol.components.combat.CombatEventSource;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class EndCombatImplementation {
    private final CombatInfoMap combatInfoMap;
    private final CombatEventSource combatEventSource;

    public EndCombatImplementation(CombatInfoMap combatInfoMap, CombatEventSource combatEventSource) {
        this.combatInfoMap = combatInfoMap;
        this.combatEventSource = combatEventSource;
    }

    CompletableFuture<Optional<CombatInfo>> tryEndCombat(Player victim) {
        return CompletableFuture.supplyAsync(
            () -> {
                AtomicReference<CombatInfo> combatInfoRef = new AtomicReference<>(null);

                this.combatInfoMap.get().computeIfPresent(victim, (Player _player, @Nullable CombatInfo prevCombatInfo) -> {
                    CombatInfo combatInfo = new CombatInfo(
                        prevCombatInfo.getStartTimestamp(), System.currentTimeMillis(), Collections.emptyMap()
                    );
                    combatInfoRef.set(combatInfo);

                    // Create an event object
                    CombatEndEvent e = new CombatEndEvent(victim, combatInfo);
                    // Propagate the event accross plugin consumers
                    this.combatEventSource.getEventBus().post(e);

                    // return newly created CombatInfo object (& update value in map).
                    return null;
                });

                return Optional.of(combatInfoRef.get());
            }
        );
    }
}
