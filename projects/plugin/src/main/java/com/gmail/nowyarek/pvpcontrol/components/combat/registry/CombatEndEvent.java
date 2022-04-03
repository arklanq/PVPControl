package com.gmail.nowyarek.pvpcontrol.components.combat.registry;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CombatEndEvent extends Event {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private final Player player;
    private final CombatInfo combatInfo;

    CombatEndEvent(Player player, CombatInfo combatInfo) {
        super(true);
        this.player = player;
        this.combatInfo = combatInfo;
    }

    public Player getPlayer() {
        return player;
    }

    public CombatInfo getCombatInfo() {
        return combatInfo;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    @SuppressWarnings("unused")
    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
