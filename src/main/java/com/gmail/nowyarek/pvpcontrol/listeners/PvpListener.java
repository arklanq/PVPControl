package com.gmail.nowyarek.pvpcontrol.listeners;

import org.bukkit.event.Listener;

public interface PvpListener extends Listener {

    void register();
    void unregister();

}
