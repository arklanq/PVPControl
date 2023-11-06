package com.gmail.nowyarek.pvpcontrol.components.combat;

import com.gmail.nowyarek.pvpcontrol.PvPControlPlugin;
import com.gmail.nowyarek.pvpcontrol.components.combat.registry.CombatInfo;
import com.gmail.nowyarek.pvpcontrol.components.combat.registry.CombatRegistry;
import com.gmail.nowyarek.pvpcontrol.components.logging.PluginLogger;
import com.google.common.collect.ImmutableMap;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nullable;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class CombatDurationTimer {
    private final static int TASK_PERIOD = 5; // ticks
    private final PvPControlPlugin plugin;
    private final Provider<CombatDurationTimerRunnable> runnableProvider;
    @Nullable
    private BukkitTask task;

    @Inject
    CombatDurationTimer(PvPControlPlugin plugin, Provider<CombatDurationTimerRunnable> runnableProvider) {
        this.plugin = plugin;
        this.runnableProvider = runnableProvider;
        plugin.getEventBus().register(this);
    }

    void start() {
        task = plugin.getServer().getScheduler().runTaskTimerAsynchronously(
            plugin, this.runnableProvider.get(), 0, TASK_PERIOD
        );
    }

    void stop() {
        if (task != null && !task.isCancelled()) task.cancel();
    }

}

class CombatDurationTimerRunnable implements Runnable {
    private final PluginLogger logger;
    private final CombatRegistry combatRegistry;

    @Inject
    CombatDurationTimerRunnable(PluginLogger logger, CombatRegistry combatRegistry) {
        this.logger = logger;
        this.combatRegistry = combatRegistry;
    }

    @Override
    public void run() {
        final long currentTimeMillis = System.currentTimeMillis();
        ImmutableMap<Player, CombatInfo> combatInfoMap = this.combatRegistry.getCombatInfoMap();

        for (Map.Entry<Player, CombatInfo> entry : combatInfoMap.entrySet()) {
            Player victim = entry.getKey();
            CombatInfo combatInfo = entry.getValue();

            try {
                // Check if it's time to end the combat at all
                if (combatInfo.getEndTimestamp() <= currentTimeMillis) {
                    try {
                        this.combatRegistry.tryEndCombat(victim).get();
                    } catch (ExecutionException e) {
                        this.logger.error(
                            String.format(
                                "Detected ExecutionException while awaiting for CombatManager#endCombat(). Player's CombatInfo (%s) processing will be skipped this time.",
                                victim.getUniqueId()
                            ), e
                        );
                    }
                } else {
                    // If not - check if we have to remove any aggressors whose combat time has passed
                    List<Entity> aggressorsList = combatInfo.getAggressorsMap().entrySet().stream()
                        .filter((Map.Entry<Entity, Long> aggressorEntry) -> aggressorEntry.getValue() <= currentTimeMillis)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());

                    if (aggressorsList.size() > 0) {
                        try {
                            CompletableFuture.allOf(
                                aggressorsList.stream()
                                    .map((Entity aggressor) -> this.combatRegistry.tryKickAggressorFromCombat(victim, aggressor))
                                    .toArray(CompletableFuture[]::new)
                            ).get();
                        } catch (ExecutionException e) {
                            this.logger.error(
                                String.format(
                                    "Detected ExecutionException while awaiting for CombatManager#tryRemoveAggressor(). Player's CombatInfo (%s) processing will be skipped this time.",
                                    victim.getUniqueId()
                                ), e
                            );
                        }
                    }
                }
            } catch (InterruptedException e) {
                this.logger.error(
                    "Detected InterruptedException while awaiting for CombatManager's or CombatInfoRegistry's async operation. Players' combat registry processing will be abandoned.", e
                );
                break;
            }
        }
    }

}