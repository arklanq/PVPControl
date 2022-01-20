package com.gmail.nowyarek.pvpcontrol;

import com.gmail.nowyarek.pvpcontrol.components.configuration.ConfigurationModule;
import com.gmail.nowyarek.pvpcontrol.components.injector.InjectorConfigurationModule;
import com.gmail.nowyarek.pvpcontrol.components.logging.LoggingModule;
import com.gmail.nowyarek.pvpcontrol.components.plugin.*;
import com.gmail.nowyarek.pvpcontrol.components.TaskChain.TaskChainModule;
import com.gmail.nowyarek.pvpcontrol.models.EventsSource;
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
        System.out.println("Server main thread: " + Thread.currentThread().getName());
        Stage stage = new PluginStageDetector(this.getLogger()).get();

        guiceInjector = Guice.createInjector(
            stage,
            binder -> binder.bind(PVPControl.class).toInstance(this),
            new InjectorConfigurationModule(),
            new PluginInfoModule(),
            new PluginEssentialsModule(),
            new LoggingModule(),
            new TaskChainModule(),
            new ConfigurationModule()
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