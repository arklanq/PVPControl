package com.gmail.nowyarek.pvpcontrol.io;

import com.gmail.nowyarek.pvpcontrol.core.PvpPlayersStore;

public class UnifiedCommandSender {
    private static PvpPlayersStore pvpPlayersStore;

    public UnifiedCommandSender(PvpPlayersStore pvpPlayersStore) {
        UnifiedCommandSender.pvpPlayersStore = pvpPlayersStore;
    }



}
