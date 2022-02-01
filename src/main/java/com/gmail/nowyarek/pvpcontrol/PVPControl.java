package com.gmail.nowyarek.pvpcontrol;

import com.gmail.nowyarek.pvpcontrol.components.TaskChain.TaskChainModule;
import com.gmail.nowyarek.pvpcontrol.components.configuration.ConfigurationModule;
import com.gmail.nowyarek.pvpcontrol.components.injector.InjectorConfigurationModule;
import com.gmail.nowyarek.pvpcontrol.components.l10n.LanguagesDetector;
import com.gmail.nowyarek.pvpcontrol.components.l10n.LocalizationModule;
import com.gmail.nowyarek.pvpcontrol.components.logging.LoggingModule;
import com.gmail.nowyarek.pvpcontrol.components.plugin.*;
import com.gmail.nowyarek.pvpcontrol.components.settings.SettingsModule;
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
        Stage stage = new PluginStageDetector(this.getLogger()).get();

        try {
            guiceInjector = Guice.createInjector(
                stage,
                binder -> binder.bind(PVPControl.class).toInstance(this),
                new InjectorConfigurationModule(),
                new PluginModule(),
                new LoggingModule(),
                new TaskChainModule(),
                new ConfigurationModule(),
                new SettingsModule(),
                new LocalizationModule()
            );
        } catch(Exception e) {
            e.printStackTrace();
            this.onDisable();
            return;
        }

        // Enable...
        this.eventBus.post(new PluginEnableEvent(this));

        // Enabled.
        this.eventBus.register(guiceInjector.getInstance(PluginEnabledEventListener.class));
        this.eventBus.post(new PluginEnabledEvent(this));

        try {
            System.out.printf("Built-in languages: %s.%n", this.guiceInjector.getInstance(LanguagesDetector.class).detectBuiltInLanguages().get());
            System.out.printf("External languages: %s.%n", this.guiceInjector.getInstance(LanguagesDetector.class).detectExternalLanguages().get());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        // Disable...
        this.eventBus.post(new PluginDisableEvent(this));

        // Disabled.
        if(this.guiceInjector != null) {
            this.eventBus.register(guiceInjector.getInstance(PluginDisabledEventListener.class));
            this.eventBus.post(new PluginDisabledEvent(this));
        }
    }

    /**
     * Available events:
     * <ul>
     *     <li>{@link PluginEnabledEvent}</li>
     *     <li>{@link PluginDisabledEvent}</li>
     * </ul>
     */
    @Override
    public EventBus getEventBus() {
        return this.eventBus;
    }
}