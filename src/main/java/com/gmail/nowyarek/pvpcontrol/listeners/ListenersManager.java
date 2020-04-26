package com.gmail.nowyarek.pvpcontrol.listeners;

import com.gmail.nowyarek.pvpcontrol.PVPControl;
import com.gmail.nowyarek.pvpcontrol.listeners.dmghandler.EntityDamageEntityListener;

import java.util.Arrays;
import java.util.List;

public class ListenersManager {
    private final List<PvpListener> listeners;

    public ListenersManager(PVPControl plugin) {
        PvpListener[] listenersArray = new PvpListener[] {
            new EntityDamageEntityListener(plugin)
        };
        listeners = Arrays.asList(listenersArray);
    }

    public void registerListeners() {
        listeners.forEach((PvpListener::register));
    }

    public void unregisterListeners() {
        listeners.forEach(PvpListener::unregister);
    }

}
