package com.example.plugin_consumer.pvpcontrol;

import com.gmail.nowyarek.pvpcontrol.components.api.PvPControl;
import com.gmail.nowyarek.pvpcontrol.components.api.PvPControlProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class PvPControl_API_Consumer extends JavaPlugin {

    public void onEnable() {
        PvPControl pvpControl = PvPControlProvider.get();
        this.getLogger().info("PvPControl version: " + pvpControl.getVersion());
    }

}
