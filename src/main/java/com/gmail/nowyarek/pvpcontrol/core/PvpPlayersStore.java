package com.gmail.nowyarek.pvpcontrol.core;

import com.gmail.nowyarek.pvpcontrol.PVPControl;
import com.gmail.nowyarek.pvpcontrol.io.Console;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PvpPlayersStore implements Listener {
    private final Console console;
    private final List<PvpPlayer> pvpPlayers = new ArrayList<>();

    public PvpPlayersStore(PVPControl plugin) {
        this.console = plugin.getConsole();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        // No unregistering - this should work until server is shut down
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPlayerJoinServer(PlayerJoinEvent e) {
        PvpPlayer pvpP = getPlayer(e.getPlayer().getUniqueId());
        if(pvpP != null) {
            console.debug("Error: Player joining server already has a duplicated profile in store.");
        } else {
            pvpP = new PvpPlayer(e.getPlayer());
            pvpPlayers.add(pvpP);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPlayerLeaveServer(PlayerQuitEvent e) {
        PvpPlayer pvpP = getPlayer(e.getPlayer().getUniqueId());
        if(pvpP == null) {
            console.debug("Error: Player leaves server, but there is no info in store about him.");
        } else {
            pvpPlayers.remove(pvpP);
        }
    }

    @NotNull
    public List<PvpPlayer> getAllPlayers() {
        return pvpPlayers;
    }

    public PvpPlayer getPlayer(@NotNull UUID playerUUID) {
        for(PvpPlayer pvpP : pvpPlayers) {
            if (pvpP.getPlayer().getUniqueId() == playerUUID)
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
