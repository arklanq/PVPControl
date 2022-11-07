package com.gmail.nowyarek.pvpcontrol.components.combat.controls;

import com.gmail.nowyarek.pvpcontrol.components.combat.CombatEventSource;
import com.gmail.nowyarek.pvpcontrol.components.combat.PlayerDamagePlayerEvent;
import com.gmail.nowyarek.pvpcontrol.components.metadata.MetaData;
import com.gmail.nowyarek.pvpcontrol.components.permissions.Permission;
import com.google.common.eventbus.Subscribe;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.EventListener;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class AdminProtectionRestrictionControl implements CombatControl {
    private final CombatEventSource combatEventSource;
    private final AtomicReference<Listener> listenerRef = new AtomicReference<>(null);

    @Inject
    AdminProtectionRestrictionControl(CombatEventSource combatEventSource) {
        this.combatEventSource = combatEventSource;
    }

    @Override
    public void integrate() {
        synchronized (this.listenerRef) {
            Listener listener = new Listener();
            this.combatEventSource.getEventBus().register(listener);
            this.listenerRef.set(listener);
        }
    }

    @Override
    public void disintegrate() {
        synchronized (this.listenerRef) {
            @Nullable
            Listener listener = this.listenerRef.get();

            if(listener != null) {
                this.combatEventSource.getEventBus().unregister(listener);
                this.listenerRef.set(null);
            }
        }
    }

    static class Listener implements EventListener {

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

}
