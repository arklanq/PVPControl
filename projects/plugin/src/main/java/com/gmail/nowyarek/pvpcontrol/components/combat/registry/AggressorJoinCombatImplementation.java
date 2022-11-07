package com.gmail.nowyarek.pvpcontrol.components.combat.registry;

import com.gmail.nowyarek.pvpcontrol.components.combat.CombatEventSource;
import com.gmail.nowyarek.pvpcontrol.components.settings.SettingsProvider;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

class AggressorJoinCombatImplementation {
    private final CombatInfoMap combatInfoMap;
    private final SettingsProvider settingsProvider;
    private final CombatEventSource combatEventSource;

    public AggressorJoinCombatImplementation(CombatInfoMap combatInfoMap, SettingsProvider settingsProvider, CombatEventSource combatEventSource) {
        this.combatInfoMap = combatInfoMap;
        this.settingsProvider = settingsProvider;
        this.combatEventSource = combatEventSource;
    }

    CompletableFuture<Optional<CombatInfo>> tryJoinAggressorToCombat(Player victim, Entity aggressor) {
        return CompletableFuture.supplyAsync(
            () -> {
                AtomicBoolean isCombatInfoUpdated = new AtomicBoolean(false);

                CombatInfo resultCombatInfo = this.combatInfoMap.get().computeIfPresent(victim, (Player _player, @Nullable CombatInfo prevCombatInfo) -> {
                    long endTimestamp = System.currentTimeMillis() + this.getCombatDurationMillis();

                    Map<Entity, Long> newAggressorsMap = new HashMap<>(prevCombatInfo.getAggressorsMap());
                    newAggressorsMap.put(aggressor, endTimestamp);

                    CombatInfo combatInfo = new CombatInfo(prevCombatInfo.getStartTimestamp(), endTimestamp, newAggressorsMap);

                    // Create an event object
                    AggressorJoinCombatEvent e = new AggressorJoinCombatEvent(victim, aggressor, combatInfo);
                    // Propagate the event accross plugin consumers
                    this.combatEventSource.getEventBus().post(e);
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

}
