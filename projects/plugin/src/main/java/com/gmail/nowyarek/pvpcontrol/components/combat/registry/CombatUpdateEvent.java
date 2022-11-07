package com.gmail.nowyarek.pvpcontrol.components.combat.registry;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CombatUpdateEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean cancelled;
    private final Player player;
    private final CombatInfo combatInfo;

    CombatUpdateEvent(Player player, CombatInfo combatInfo) {
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
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
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
