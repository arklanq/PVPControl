package com.gmail.nowyarek.pvpcontrol.components.combat;

import com.gmail.nowyarek.pvpcontrol.components.combat.registry.AggressorJoinCombatEvent;
import com.gmail.nowyarek.pvpcontrol.components.combat.registry.AggressorLeaveCombatEvent;
import com.gmail.nowyarek.pvpcontrol.components.combat.registry.CombatEndEvent;
import com.gmail.nowyarek.pvpcontrol.components.combat.registry.CombatStartEvent;
import com.gmail.nowyarek.pvpcontrol.components.logging.PluginLogger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import jakarta.inject.Inject;

class EventsTester implements Listener {
    private final PluginLogger logger;

    @Inject
    EventsTester(PluginLogger logger, JavaPlugin plugin) {
        this.logger = logger;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    void onCombatStartEvent(CombatStartEvent e) {
        this.logger.debug(e.toString());
    }

    @EventHandler
    void onCombatEndEvent(CombatEndEvent e) {
        this.logger.debug(e.toString());
    }

    @EventHandler
    void onAggressorJoinCombatEvent(AggressorJoinCombatEvent e) {
        this.logger.debug(e.toString());
    }

    @EventHandler
    void onAggressorLeaveCombatEvent(AggressorLeaveCombatEvent e) {
        this.logger.debug(e.toString());
    }

}
