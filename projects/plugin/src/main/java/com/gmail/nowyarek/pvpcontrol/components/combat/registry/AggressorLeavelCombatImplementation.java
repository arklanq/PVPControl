package com.gmail.nowyarek.pvpcontrol.components.combat.registry;

import com.gmail.nowyarek.pvpcontrol.components.combat.CombatEventSource;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class AggressorLeavelCombatImplementation {
    private final CombatInfoMap combatInfoMap;
    private final CombatEventSource combatEventSource;

    public AggressorLeavelCombatImplementation(CombatInfoMap combatInfoMap, CombatEventSource combatEventSource) {
        this.combatInfoMap = combatInfoMap;
        this.combatEventSource = combatEventSource;
    }

    CompletableFuture<Optional<CombatInfo>> tryKickAggressorFromCombat(Player victim, Entity aggressor) {
        return CompletableFuture.supplyAsync(
            () -> {
                @Nullable
                CombatInfo resultCombatInfo = this.combatInfoMap.get().computeIfPresent(victim, (Player _player, @Nullable CombatInfo prevCombatInfo) -> {
                    Map<Entity, Long> newAggressorsMap = new HashMap<>(prevCombatInfo.getAggressorsMap());
                    newAggressorsMap.remove(aggressor);

                    CombatInfo combatInfo = new CombatInfo(prevCombatInfo.getStartTimestamp(), prevCombatInfo.getEndTimestamp(), newAggressorsMap);

                    // Create an event object
                    AggressorLeaveCombatEvent e = new AggressorLeaveCombatEvent(victim, aggressor, combatInfo);
                    // Propagate the event accross plugin consumers
                    this.combatEventSource.getEventBus().post(e);

                    // return newly created CombatInfo object (& update value in map).
                    return combatInfo;
                });

                return Optional.ofNullable(resultCombatInfo);
            }
        );
    }
}
