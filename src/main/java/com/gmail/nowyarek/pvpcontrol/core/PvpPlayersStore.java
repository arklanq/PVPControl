package com.gmail.nowyarek.pvpcontrol.core;

import com.gmail.nowyarek.pvpcontrol.PVPControl;
import com.gmail.nowyarek.pvpcontrol.io.Console;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PvpPlayersStore implements Listener {
    private final PVPControl plugin;
    private final List<PvpPlayer> pvpPlayers = new ArrayList<>();

    public PvpPlayersStore(PVPControl plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        // No unregistering - this should work until server is shut down
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPlayerJoinServer(PlayerJoinEvent e) {
        PvpPlayer pvpP = getPvpPlayer(e.getPlayer().getUniqueId());
        if(pvpP != null) {
            Console.debug("Error: Player joining server already has a duplicated profile in store.");
        } else {
            pvpP = new PvpPlayer(plugin, e.getPlayer());
            pvpPlayers.add(pvpP);
            Console.debug(
                String.format("Player `%s` is admin: %s.", pvpP.getPlayerEntity().getName(), pvpP.isPluginAdmin())
            );
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPlayerLeaveServer(PlayerQuitEvent e) {
        PvpPlayer pvpP = getPvpPlayer(e.getPlayer().getUniqueId());
        if(pvpP == null) {
            Console.debug("Error: Player leaves server, but there is no info in store about him.");
        } else {
            pvpPlayers.remove(pvpP);
            pvpP.dispose();
        }
    }

    @NotNull
    public List<PvpPlayer> getAllPlayers() {
        return pvpPlayers;
    }

    @Nullable
    public PvpPlayer getPvpPlayer(@NotNull UUID playerUUID) {
        for(PvpPlayer pvpP : pvpPlayers) {
            if (pvpP.getPlayerEntity().getUniqueId() == playerUUID)
                return pvpP;
        }
        return null;
    }

    public void lockBufferForAllPlayers() {
        pvpPlayers.forEach(PvpPlayer::lockBuffer);
    }

    public void releaseBufferForAllPlayers() {
        pvpPlayers.forEach(PvpPlayer::releaseBuffer);
    }

}
