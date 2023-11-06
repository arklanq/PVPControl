package com.gmail.nowyarek.pvpcontrol.components.combat;

import com.gmail.nowyarek.pvpcontrol.components.metadata.MetaData;
import com.gmail.nowyarek.pvpcontrol.components.permissions.Permission;
import com.google.common.eventbus.Subscribe;
import org.bukkit.entity.Player;

import jakarta.inject.Inject;
import java.util.EventListener;
import java.util.stream.Stream;

public class AdminProtectionRestrictionControl implements CombatControl, EventListener {
    private final CombatEventSource combatEventSource;

    @Inject
    AdminProtectionRestrictionControl(CombatEventSource combatEventSource) {
        this.combatEventSource = combatEventSource;
    }

    @Override
    public void integrate() {
        this.combatEventSource.getEventBus().register(this);
    }

    @Override
    public void disintegrate() {
        this.combatEventSource.getEventBus().unregister(this);
    }

    @Subscribe
    void onPlayerDamagePlayerEvent(PlayerDamagePlayerEvent e) {
        // Return if the any player is a staff member under protection
        Stream.of(e.getVictim(), e.getDamager()).forEach(((Player p) -> {
            if(
                p.hasPermission(Permission.Commands.TOGGLE.value())
                && p.hasMetadata(MetaData.Bypass.OP_PROTECTION.value())
            ) e.setCancelled(true);
        }));
    }

}
