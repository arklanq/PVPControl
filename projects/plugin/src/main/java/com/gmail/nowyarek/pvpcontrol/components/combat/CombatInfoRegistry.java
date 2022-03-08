package com.gmail.nowyarek.pvpcontrol.components.combat;

import com.gmail.nowyarek.pvpcontrol.components.settings.SettingsProvider;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class CombatInfoRegistry {
    private final ConcurrentHashMap<Player, CombatInfo> combatInfoMap = new ConcurrentHashMap<>();
    private final SettingsProvider settingsProvider;

    @Inject
    public CombatInfoRegistry(SettingsProvider settingsProvider) {
        this.settingsProvider = settingsProvider;
    }

    Optional<CombatInfo> getCombatInfo(Player player) {
        return Optional.ofNullable(this.combatInfoMap.get(player));
    }

    ImmutableMap<Player, CombatInfo> getCombatInfoMap() {
        return ImmutableMap.copyOf(combatInfoMap);
    }

    /**
     * @param victim - the player who is the epicenter of the combat
     * @return newly created CombatInfo object if combat has just started or existing CombatInfo object if there was already a combat in progress
     */
    CompletableFuture<CombatInfo> startCombat(Player victim) {
        return CompletableFuture.supplyAsync(
            () -> this.combatInfoMap.compute(victim, (Player _player, @Nullable CombatInfo prevCombatInfo) -> {
                long endTimestamp = System.currentTimeMillis() + this.getCombatDurationMillis();

                if (prevCombatInfo == null)
                    return new CombatInfo(System.currentTimeMillis(), endTimestamp, Collections.emptyMap());
                else {
                    return new CombatInfo(prevCombatInfo.getStartTimestamp(), endTimestamp, prevCombatInfo.getAggressorsMap());
                }
            })
        );
    }

    /**
     * @param victim     - the player who is the epicenter of the combat
     * @param aggressors - an array of entities that carry out the attack
     * @return newly created CombatInfo object if combat has just started or existing CombatInfo object if there was already a combat in progress
     */
    CompletableFuture<CombatInfo> addAggressors(Player victim, Entity[] aggressors) {
        return CompletableFuture.supplyAsync(
            () -> this.combatInfoMap.compute(victim, (Player _player, @Nullable CombatInfo prevCombatInfo) -> {
                long currentTimeMillis = System.currentTimeMillis();
                long endTimestamp = currentTimeMillis + this.getCombatDurationMillis();

                Map<Entity, Long> aggressorsMap = Arrays.stream(aggressors).collect(
                    Collectors.toMap((Entity aggressor) -> aggressor, (Entity aggressor) -> endTimestamp)
                );

                if (prevCombatInfo == null) {
                    return new CombatInfo(currentTimeMillis, endTimestamp, aggressorsMap);
                } else {
                    ImmutableSet<Entity> aggressorsSet = ImmutableSet.copyOf(aggressors);
                    Stream<Map.Entry<Entity, Long>> filteredAggressorsMapStream = prevCombatInfo.getAggressorsMap()
                        .entrySet().stream()
                        .filter(
                            (Map.Entry<Entity, Long> entry) -> (
                                aggressorsSet.stream().noneMatch(
                                    (Entity aggressor) -> aggressor.getUniqueId().compareTo(entry.getKey().getUniqueId()) == 0
                                )
                            )
                        );

                    Map<Entity, Long> newAggressorsMap = Stream
                        .concat(
                            filteredAggressorsMapStream,
                            aggressorsMap.entrySet().stream()
                        )
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                    return new CombatInfo(prevCombatInfo.getStartTimestamp(), endTimestamp, newAggressorsMap);
                }
            })
        );
    }

    /**
     * @param victim     - the player who is the epicenter of the combat
     * @param aggressors - an array of entities that are about to leave the combat
     * @return updated CombatInfo object or null if there wasn't any active combat (wrapped in Optional)
     */
    CompletableFuture<Optional<CombatInfo>> removeAggressors(Player victim, Entity[] aggressors) {
        return CompletableFuture.supplyAsync(
            () -> {
                @Nullable
                CombatInfo combatInfo = this.combatInfoMap.computeIfPresent(victim, (Player _player, CombatInfo prevCombatInfo) -> {
                    // Collect basic data to construct CombatInfo
                    long startTimestamp = prevCombatInfo.getStartTimestamp();
                    long endTimestamp = System.currentTimeMillis();
                    List<Entity> aggressorsList = Arrays.asList(aggressors);

                    Map<Entity, Long> newAggressorsMap = prevCombatInfo.getAggressorsMap().entrySet().stream()
                        .filter(
                            (Map.Entry<Entity, Long> entry) -> (
                                aggressorsList.stream().anyMatch(
                                    (Entity aggressor) -> aggressor.getUniqueId().compareTo(entry.getKey().getUniqueId()) == 0
                                )
                            )
                        )
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                    return new CombatInfo(startTimestamp, endTimestamp, newAggressorsMap);
                });

                return Optional.ofNullable(combatInfo);
            }
        );
    }

    /**
     * @param victim - the player who is the epicenter of the combat
     * @return updated CombatInfo object or null if there wasn't any active combat (wrapped in Optional).
     */
    CompletableFuture<Optional<CombatInfo>> endCombat(Player victim) {
        return CompletableFuture.supplyAsync(() -> {
            AtomicReference<CombatInfo> combatInfo = new AtomicReference<>(null);

            this.combatInfoMap.computeIfPresent(victim, (Player _player, CombatInfo prevCombatInfo) -> {
                // Collect basic data to construct CombatInfo
                long startTimestamp = prevCombatInfo.getStartTimestamp();
                long endTimestamp = System.currentTimeMillis();

                // Construct new CombatInfo object and pass to enclosing closure
                combatInfo.set(new CombatInfo(startTimestamp, endTimestamp, Collections.emptyMap()));

                // ... and unmap specified Player key (remove from map)
                return null;
            });

            return Optional.ofNullable(combatInfo.get());
        });
    }

    private long getCombatDurationMillis() {
        return this.settingsProvider.get().PvP().getCombatDuration() * 1000L;
    }

}
