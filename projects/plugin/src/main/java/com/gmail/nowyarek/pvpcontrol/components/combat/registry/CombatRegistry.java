package com.gmail.nowyarek.pvpcontrol.components.combat.registry;

import com.gmail.nowyarek.pvpcontrol.models.EventSource;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.eventbus.EventBus;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Singleton
public class CombatRegistry implements EventSource {
    private final EventBus eventBus = new EventBus();
    private final CombatInfoMap combatInfoMap;
    private final StartCombatImplementation startCombatImplementation;
    private final AggressorJoinCombatImplementation aggressorJoinCombatImplementation;
    private final TriggerCombatImplementation triggerCombatImplementation;
    private final AggressorLeavelCombatImplementation aggressorLeavelCombatImplementation;
    private final EndCombatImplementation endCombatImplementation;

    @Inject
    public CombatRegistry(
        CombatInfoMap combatInfoMap,
        StartCombatImplementation startCombatImplementation,
        AggressorJoinCombatImplementation aggressorJoinCombatImplementation,
        TriggerCombatImplementation triggerCombatImplementation,
        AggressorLeavelCombatImplementation aggressorLeavelCombatImplementation,
        EndCombatImplementation endCombatImplementation
    ) {
        this.combatInfoMap = combatInfoMap;
        this.startCombatImplementation = startCombatImplementation;
        this.aggressorJoinCombatImplementation = aggressorJoinCombatImplementation;
        this.triggerCombatImplementation = triggerCombatImplementation;
        this.aggressorLeavelCombatImplementation = aggressorLeavelCombatImplementation;
        this.endCombatImplementation = endCombatImplementation;
    }

    public Optional<CombatInfo> getCombatInfo(Player player) {
        return Optional.ofNullable(this.combatInfoMap.get().get(player));
    }

    public boolean isPlayerDuringCombat(Player p) {
        return this.getCombatInfo(p).isPresent();
    }

    public ImmutableMap<Player, CombatInfo> getCombatInfoMap() {
        return ImmutableMap.copyOf(combatInfoMap.get());
    }

    public ImmutableList<Player> getAllPlayersDuringCombat() {
        return ImmutableList.copyOf(this.combatInfoMap.get().keySet());
    }

    /**
     * @param victim - the {@link Player} who is the epicenter of the combat
     * @return {@link CombatInfo} object or null (wrapped in Optional) depending on situation:
     * <ul>
     *  <li>{@link CombatInfo} object if combat has just started and {@link CombatStartEvent} has not been cancelled.</li>
     *  <li>`null` if combat was about to start but {@link CombatStartEvent} has been cancelled.</li>
     *  <li>`null` if there was already a combat in progress.</li>
     * </ul>
     */
    public CompletableFuture<Optional<CombatInfo>> tryStartCombat(Player victim) {
        return this.startCombatImplementation.tryStartCombat(victim);
    }

    /**
     * @param victim    - the player who is the epicenter of the combat
     * @param aggressor - entity that carry out the attack
     * @return {@link CombatInfo} object or null (wrapped in Optional) depending on situation:
     * <ul>
     *  <li>{@link CombatInfo} object if combat is in progress and {@link AggressorJoinCombatEvent} has not been cancelled.</li>
     *  <li>`null` if combat is in progress but {@link AggressorJoinCombatEvent} has been cancelled.</li>
     *  <li>`null` if there was no combat at all.</li>
     * </ul>
     */
    public CompletableFuture<Optional<CombatInfo>> tryJoinAggressorToCombat(Player victim, Entity aggressor) {
        return this.aggressorJoinCombatImplementation.tryJoinAggressorToCombat(victim, aggressor);
    }

    /**
     * @param victim    - the player who is the epicenter of the combat
     * @param aggressors - array of entities that carry out the attack
     * @return {@link CombatInfo} object or null (wrapped in Optional) depending on situation:
     * <ul>
     *  <li>{@link CombatInfo} object if combat has just started and {@link CombatStartEvent} has not been cancelled.</li>
     *  <li>{@link CombatInfo} object if combat is in progress and there is at least one new aggressor whose {@link AggressorJoinCombatEvent} has not been cancelled.</li>
     *  <li>`null` if combat was about to start but {@link CombatStartEvent} has been cancelled.</li>
     *  <li>`null` if combat is in progress and there are no new aggressors (aggressors array is empty or {@link AggressorJoinCombatEvent} was cancelled for every one of them).</li>
     * </ul>
     */
    public CompletableFuture<Optional<CombatInfo>> triggerCombat(Player victim, Entity[] aggressors) {
        return this.triggerCombatImplementation.triggerCombat(victim, aggressors);
    }

    /**
     * @param victim    - the player who is the epicenter of the combat
     * @param aggressor - an entity that is about to leave the combat
     * @return updated CombatInfo object or null (wrapped in Optional) if there wasn't any active combat (wrapped in Optional)
     */
    public CompletableFuture<Optional<CombatInfo>> tryKickAggressorFromCombat(Player victim, Entity aggressor) {
        return this.aggressorLeavelCombatImplementation.tryKickAggressorFromCombat(victim, aggressor);
    }

    /**
     * @param victim - the player who is the epicenter of the combat
     * @return updated CombatInfo object or null (wrapped in Optional) if there wasn't any active combat (wrapped in Optional).
     */
    public CompletableFuture<Optional<CombatInfo>> tryEndCombat(Player victim) {
        return this.endCombatImplementation.tryEndCombat(victim);
    }

    @Override
    public EventBus getEventBus() {
        return this.eventBus;
    }
}
