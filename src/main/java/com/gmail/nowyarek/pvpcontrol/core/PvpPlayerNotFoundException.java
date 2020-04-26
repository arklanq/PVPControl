package com.gmail.nowyarek.pvpcontrol.core;

import org.bukkit.entity.Player;

import java.util.UUID;

// Not really used right now..
public class PvpPlayerNotFoundException extends RuntimeException {

    public PvpPlayerNotFoundException(Player p) {
        super(String.format("PvpPlayer (Name:%s | UUID:%s) not found in store.", p.getName(), p.getUniqueId()));
    }

    public PvpPlayerNotFoundException(UUID uuid) {
        super(String.format("PvpPlayer (UUID:%s) not found in store.", uuid));
    }

}
