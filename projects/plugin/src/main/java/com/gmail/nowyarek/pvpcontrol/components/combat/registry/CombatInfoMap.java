package com.gmail.nowyarek.pvpcontrol.components.combat.registry;

import org.bukkit.entity.Player;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
class CombatInfoMap implements Provider<ConcurrentHashMap<Player, CombatInfo>> {
    private final ConcurrentHashMap<Player, CombatInfo> combatInfoMap = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Player, CombatInfo> get() {
        return this.combatInfoMap;
    }
}
