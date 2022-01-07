package com.gmail.nowyarek.pvpcontrol.listeners;

import co.aikar.taskchain.TaskChainFactory;
import com.gmail.nowyarek.pvpcontrol.annotations.PluginVersion;
import com.gmail.nowyarek.pvpcontrol.events.PluginDisableEvent;
import com.gmail.nowyarek.pvpcontrol.events.PluginEnableEvent;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import org.bukkit.Server;

import java.util.EventListener;
import java.util.concurrent.TimeUnit;

public class PluginDisableEventListener implements EventListener {
    private final TaskChainFactory taskChainFactory;

    @Inject
    public PluginDisableEventListener(TaskChainFactory taskChainFactory) {
        this.taskChainFactory = taskChainFactory;
    }

    @Subscribe
    public void onEvent(PluginDisableEvent e) {
        this.taskChainFactory.shutdown(2500, TimeUnit.MILLISECONDS);
        e.getPlugin().getServer().getConsoleSender().sendMessage(
            String.format("§7%s disabled.", e.getPlugin().getName())
        );
    }

}
