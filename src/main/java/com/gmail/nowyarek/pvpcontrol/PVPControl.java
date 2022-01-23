package com.gmail.nowyarek.pvpcontrol;

import com.gmail.nowyarek.pvpcontrol.components.configuration.ConfigurationModule;
import com.gmail.nowyarek.pvpcontrol.components.settings.SettingsModule;
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

import javax.annotation.Nullable;

public class PVPControl extends JavaPlugin implements EventsSource {
    private final EventBus eventBus = new EventBus();
    @Nullable private Injector guiceInjector;

    @Override
    public void onEnable() {
        Stage stage = new PluginStageDetector(this.getLogger()).get();

        guiceInjector = Guice.createInjector(
            stage,
            binder -> binder.bind(PVPControl.class).toInstance(this),
            new InjectorConfigurationModule(),
            new PluginModule(),
            new LoggingModule(),
            new TaskChainModule(),
            new ConfigurationModule(),
            new SettingsModule()
        );

        this.eventBus.register(guiceInjector.getInstance(PluginEnableEventListener.class));
        this.eventBus.post(new PluginEnableEvent(this));
    }

    @Override
    public void onDisable() {
        if(this.guiceInjector != null) {
            this.eventBus.register(guiceInjector.getInstance(PluginDisableEventListener.class));
            this.eventBus.post(new PluginDisableEvent(this));
        }
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