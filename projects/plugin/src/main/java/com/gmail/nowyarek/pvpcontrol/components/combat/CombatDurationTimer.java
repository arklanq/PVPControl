package com.gmail.nowyarek.pvpcontrol.components.combat;

import com.gmail.nowyarek.pvpcontrol.PvPControlPlugin;
import com.gmail.nowyarek.pvpcontrol.components.logging.PluginLogger;
import com.gmail.nowyarek.pvpcontrol.components.plugin.PluginDisableEvent;
import com.gmail.nowyarek.pvpcontrol.components.plugin.PluginEnableEvent;
import com.google.common.collect.ImmutableMap;
import com.google.common.eventbus.Subscribe;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.EventListener;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Singleton
public class CombatDurationTimer implements EventListener {
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

    @Subscribe
    void onPluginEnable(PluginEnableEvent e) {
        task = plugin.getServer().getScheduler().runTaskTimerAsynchronously(
            plugin, this.runnableProvider.get(), 0, TASK_PERIOD
        );
    }

    @Subscribe
    void onPluginDisable(PluginDisableEvent e) {
        if(task != null && !task.isCancelled()) task.cancel();
    }

}

class CombatDurationTimerRunnable implements Runnable {
    private final PluginLogger logger;
    private final CombatInfoRegistry combatInfoRegistry;
    private final CombatManager combatManager;

    @Inject
    CombatDurationTimerRunnable(PluginLogger logger, CombatInfoRegistry combatInfoRegistry, CombatManager combatManager) {
        this.logger = logger;
        this.combatInfoRegistry = combatInfoRegistry;
        this.combatManager = combatManager;
    }

    @Override
    public void run() {
        final long currentTimeMillis = System.currentTimeMillis();
        ImmutableMap<Player, CombatInfo> combatInfoMap = this.combatInfoRegistry.getCombatInfoMap();

        for (Map.Entry<Player, CombatInfo> entry : combatInfoMap.entrySet()) {
            Player victim = entry.getKey();
            CombatInfo combatInfo = entry.getValue();

            this.logger.debug(String.format("Victim %s, CombatInfo %s", victim, combatInfo));

            try {
                // Check if it's time to end the combat at all
                if(combatInfo.getEndTimestamp() <= currentTimeMillis) {
                    try {
                        this.combatManager.endCombat(victim).get();
                    } catch(ExecutionException e) {
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

                    if(aggressorsList.size() > 0) {
                        try {
                            this.combatInfoRegistry.removeAggressors(victim, aggressorsList.toArray(new Entity[0])).get();
                        } catch(ExecutionException e) {
                            this.logger.error(
                                String.format(
                                    "Detected ExecutionException while awaiting for CombatManager#removeAggressors(). Player's CombatInfo (%s) processing will be skipped this time.",
                                    victim.getUniqueId()
                                ), e
                            );
                        }
                    }
                }
            } catch(InterruptedException e) {
                this.logger.error(
                    "Detected InterruptedException while awaiting for CombatManager's or CombatInfoRegistry's async operation. Players' combat registry processing will be abandoned.", e
                );
                break;
            }
        }
    }

}