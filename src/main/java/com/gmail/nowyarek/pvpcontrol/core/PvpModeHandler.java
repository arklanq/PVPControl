package com.gmail.nowyarek.pvpcontrol.core;

import com.gmail.nowyarek.pvpcontrol.PVPControl;
import com.gmail.nowyarek.pvpcontrol.configuration.MainConfig;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class PvpModeHandler {
    @NotNull
    private final PVPControl plugin;
    @Nullable
    private BukkitTask bukkitTimer;
    int pvpDuration;
    @NotNull
    final HashMap<UUID, Long> playersInCombat = new HashMap<>();

    public PvpModeHandler(@NotNull PVPControl plugin) {
        this.plugin = plugin;
    }
    public void initialize() {
        MainConfig mainConfig = plugin.getConfigurationManager().mainConfig;
        pvpDuration = mainConfig.content.PVP.TimeInPVP;
        startTimer();
    }
    public void reinitialize() { // do not deinitialize, instead of make quick swap
        initialize();
    }
    public void deinitialize() {
        stopTimer();
    }

    private void removeFromCombat(@NotNull UUID uuid) {
        // This should be never null - there is no concurrency to do such a bug
        Objects.requireNonNull(plugin.getPlayersStore().getPvpPlayer(uuid)).getOutOfTheFight();
    }

    private void startTimer() {
        stopTimer();
        bukkitTimer = new CombatModeTimer(
            playersInCombat,
            this::removeFromCombat,
            pvpDuration
        ).runTaskTimerAsynchronously(plugin, 5, 5);
    }
    private void stopTimer() {
        if(bukkitTimer != null) {
            bukkitTimer.cancel();
            bukkitTimer = null;
        }
    }

}
