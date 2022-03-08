package com.gmail.nowyarek.pvpcontrol.components.combat;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlayerDamagePlayerEvent implements Cancellable {
    private final Player victim, damager;
    private boolean cancel;

    public PlayerDamagePlayerEvent(Player victim, Player damager) {
        this.victim = victim;
        this.damager = damager;
    }

    public Player getVictim() {
        return victim;
    }

    public Player getDamager() {
        return damager;
    }

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
