package com.gmail.nowyarek.pvpcontrol;

import com.gmail.nowyarek.pvpcontrol.events.PluginDisableEvent;
import com.gmail.nowyarek.pvpcontrol.events.PluginEnableEvent;
import com.gmail.nowyarek.pvpcontrol.interfaces.EventsSource;
import com.gmail.nowyarek.pvpcontrol.listeners.PluginDisableEventListener;
import com.gmail.nowyarek.pvpcontrol.listeners.PluginEnableEventListener;
import com.gmail.nowyarek.pvpcontrol.modules.InjectorConfigurationModule;
import com.gmail.nowyarek.pvpcontrol.modules.PluginInfoModule;
import com.gmail.nowyarek.pvpcontrol.modules.PluginEssentialsModule;
import com.gmail.nowyarek.pvpcontrol.utils.PluginStageDetector;
import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import org.bukkit.plugin.java.JavaPlugin;

public class PVPControl extends JavaPlugin implements EventsSource {
    private final EventBus eventBus = new EventBus();
    private Injector guiceInjector;

    @Override
    public void onEnable() {
        Stage stage = new PluginStageDetector(this.getLogger()).findOutStage();

        guiceInjector = Guice.createInjector(
            stage,
            binder -> binder.bind(PVPControl.class).toInstance(this),
            new InjectorConfigurationModule(),
            new PluginInfoModule(),
            new PluginEssentialsModule()
        );

        this.eventBus.register(guiceInjector.getInstance(PluginEnableEventListener.class));
        this.eventBus.post(new PluginEnableEvent(this));
    }

    @Override
    public void onDisable() {
        this.eventBus.register(guiceInjector.getInstance(PluginDisableEventListener.class));
        this.eventBus.post(new PluginDisableEvent(this));
    }

    /**
     * Available events:
     * <ul>
     *     <li>{@link PluginEnableEvent}</li>
     *     <li>{@link PluginDisableEvent}</li>
     * </ul>
     */
    @Override
    public EventBus getEventBus() {
        return this.eventBus;
    }
}