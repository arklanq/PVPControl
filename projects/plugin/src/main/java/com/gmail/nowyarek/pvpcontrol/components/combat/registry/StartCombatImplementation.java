package com.gmail.nowyarek.pvpcontrol.components.combat.registry;

import com.gmail.nowyarek.pvpcontrol.components.settings.SettingsProvider;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import jakarta.inject.Inject;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

class StartCombatImplementation {
    private final ConcurrentHashMap<Player, CombatInfo> combatInfoMap;
    private final SettingsProvider settingsProvider;
    private final Server server;

    public StartCombatImplementation(ConcurrentHashMap<Player, CombatInfo> combatInfoMap, SettingsProvider settingsProvider, Server server) {
        this.combatInfoMap = combatInfoMap;
        this.settingsProvider = settingsProvider;
        this.server = server;
    }

    CompletableFuture<Optional<CombatInfo>> tryStartCombat(Player victim) {
        return CompletableFuture.supplyAsync(
            () -> {
                AtomicBoolean isActionTaken = new AtomicBoolean(false);

                @Nullable
                CombatInfo resultCombatInfo = this.combatInfoMap.computeIfAbsent(victim, (Player _player) -> {
                    // Calculate combat end timestamp
                    long endTimestamp = System.currentTimeMillis() + this.getCombatDurationMillis();

                    // Create new CombatInfo object
                    CombatInfo combatInfo = new CombatInfo(System.currentTimeMillis(), endTimestamp, Collections.emptyMap());

                    // Create an event object
                    CombatStartEvent e = new CombatStartEvent(victim, combatInfo);
                    // Propagate the event accross plugin consumers
                    this.server.getPluginManager().callEvent(e);
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

    static class Factory {
        private final SettingsProvider settingsProvider;
        private final Server server;

        @Inject
        Factory(SettingsProvider settingsProvider, JavaPlugin plugin) {
            this.settingsProvider = settingsProvider;
            this.server = plugin.getServer();
        }

        StartCombatImplementation create(ConcurrentHashMap<Player, CombatInfo> combatInfoMap) {
            return new StartCombatImplementation(combatInfoMap, this.settingsProvider, this.server);
        }

    }

}
