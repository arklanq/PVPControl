package com.gmail.nowyarek.pvpcontrol.components.combat;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CombatEndEvent extends Event {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private final Player player;

    CombatEndEvent(Player player) {
        super(true);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }
}
