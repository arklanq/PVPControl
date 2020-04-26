package com.gmail.nowyarek.pvpcontrol.core;

import org.bukkit.scheduler.BukkitRunnable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

public class CombatModeTimer extends BukkitRunnable {
    private final HashMap<UUID, Long> playersInCombat;
    private final Consumer<UUID> removeFromCombat;
    private final int pvpDuration;

    public CombatModeTimer(HashMap<UUID, Long> playersInCombat, Consumer<UUID> removeFromCombat, int pvpDuration) {
        this.playersInCombat = playersInCombat;
        this.removeFromCombat = removeFromCombat;
        this.pvpDuration = pvpDuration;
    }

    @Override
    public void run() {
        long currentTimeMillis = System.currentTimeMillis();
        ArrayList<UUID> playersToRemove = new ArrayList<>();
        for(UUID uuid : playersInCombat.keySet()) {
            if((playersInCombat.get(uuid) + pvpDuration * 1000) < currentTimeMillis) {
                playersToRemove.add(uuid);
            }
        }

        for(UUID uuid : playersToRemove) {
            removeFromCombat.accept(uuid);
        }
    }

}
