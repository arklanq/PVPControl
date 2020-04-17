package com.gmail.nowyarek.pvpcontrol.core;

import org.bukkit.entity.Player;

public class PvpPlayerNotFoundException extends RuntimeException {

    public PvpPlayerNotFoundException(Player p) {
        super(String.format("PvpPlayer (Name:%s | UUID:%s) not found in store.", p.getName(), p.getUniqueId()));
    }

}
