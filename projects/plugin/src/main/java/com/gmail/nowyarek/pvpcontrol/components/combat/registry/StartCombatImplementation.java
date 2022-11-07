package com.gmail.nowyarek.pvpcontrol.components.combat.registry;

import com.gmail.nowyarek.pvpcontrol.components.combat.CombatEventSource;
import com.gmail.nowyarek.pvpcontrol.components.settings.SettingsProvider;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

class StartCombatImplementation {
    private final CombatInfoMap combatInfoMap;
    private final SettingsProvider settingsProvider;
    private final CombatEventSource combatEventSource;

    public StartCombatImplementation(CombatInfoMap combatInfoMap, SettingsProvider settingsProvider, CombatEventSource combatEventSource) {
        this.combatInfoMap = combatInfoMap;
        this.settingsProvider = settingsProvider;
        this.combatEventSource = combatEventSource;
    }

    CompletableFuture<Optional<CombatInfo>> tryStartCombat(Player victim) {
        return CompletableFuture.supplyAsync(
            () -> {
                AtomicBoolean isActionTaken = new AtomicBoolean(false);

                @Nullable
                CombatInfo resultCombatInfo = this.combatInfoMap.get().computeIfAbsent(victim, (Player _player) -> {
                    // Calculate combat end timestamp
                    long endTimestamp = System.currentTimeMillis() + this.getCombatDurationMillis();

                    // Create new CombatInfo object
                    CombatInfo combatInfo = new CombatInfo(System.currentTimeMillis(), endTimestamp, Collections.emptyMap());

                    // Create an event object
                    CombatStartEvent e = new CombatStartEvent(victim, combatInfo);
                    // Propagate the event accross plugin consumers
                    this.combatEventSource.getEventBus().post(e);
                    // If any of the plugin consumers cancels the event then immediately return with null (do not map key).
                    if(e.isCancelled()) return null;

                    isActionTaken.set(true);
                    // return newly created CombatInfo object (& put new value into map).
                    return combatInfo;
                });

                if(isActionTaken.get())
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
