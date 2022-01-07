package com.gmail.nowyarek.pvpcontrol;

import com.gmail.nowyarek.pvpcontrol.events.PluginDisableEvent;
import com.gmail.nowyarek.pvpcontrol.events.PluginEnableEvent;
import com.gmail.nowyarek.pvpcontrol.interfaces.EventsSource;
import com.gmail.nowyarek.pvpcontrol.listeners.PluginDisableEventListener;
import com.gmail.nowyarek.pvpcontrol.listeners.PluginEnableEventListener;
import com.gmail.nowyarek.pvpcontrol.modules.InjectorConfigurationModule;
import com.gmail.nowyarek.pvpcontrol.modules.PluginInfoModule;
import com.gmail.nowyarek.pvpcontrol.modules.TaskChainsConfigurationModule;
import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.bukkit.plugin.java.JavaPlugin;

public class PVPControl extends JavaPlugin implements EventsSource {
    private final EventBus eventBus = new EventBus();

    @Override
    public void onEnable() {
        Injector guice = Guice.createInjector(
            new InjectorConfigurationModule(),
            new PluginInfoModule(this),
            new TaskChainsConfigurationModule()
        );

        this.eventBus.register(guice.getInstance(PluginEnableEventListener.class));
        this.eventBus.register(guice.getInstance(PluginDisableEventListener.class));

        this.eventBus.post(new PluginEnableEvent(this));
    }

    @Override
    public void onDisable() {
        this.eventBus.post(new PluginDisableEvent(this));
    }

    @Override
    public EventBus getEventBus() {
        return this.eventBus;
    }
}