package com.gmail.nowyarek.pvpcontrol;

import com.gmail.nowyarek.pvpcontrol.components.TaskChain.TaskChainModule;
import com.gmail.nowyarek.pvpcontrol.components.combat.CombatModule;
import com.gmail.nowyarek.pvpcontrol.components.combat.registry.CombatRegistryModule;
import com.gmail.nowyarek.pvpcontrol.components.commands.CommandsModule;
import com.gmail.nowyarek.pvpcontrol.components.configuration.ConfigurationModule;
import com.gmail.nowyarek.pvpcontrol.components.injector.InjectorConfigurationModule;
import com.gmail.nowyarek.pvpcontrol.components.l10n.LocalizationModule;
import com.gmail.nowyarek.pvpcontrol.components.logging.LoggingModule;
import com.gmail.nowyarek.pvpcontrol.components.logging.PluginLogger;
import com.gmail.nowyarek.pvpcontrol.components.plugin.*;
import com.gmail.nowyarek.pvpcontrol.components.settings.SettingsModule;
import com.gmail.nowyarek.pvpcontrol.models.EventSource;
import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import org.bukkit.plugin.java.JavaPlugin;

public class PvPControlPlugin extends JavaPlugin implements EventSource {
    private final EventBus eventBus = new EventBus();
    private Injector guiceInjector;

    @Override
    public void onEnable() {
        // Detect stage (aka mode)
        Stage stage = new PluginStageDetector(this.getLogger()).get();

        // Create Guice injector
        try {
            guiceInjector = Guice.createInjector(
                stage,
                binder -> binder.bind(PvPControlPlugin.class).toInstance(this),
                new InjectorConfigurationModule(),
                new PluginModule(),
                new LoggingModule(),
                new TaskChainModule(),
                new ConfigurationModule(),
                new SettingsModule(),
                new LocalizationModule(),
                new CombatRegistryModule(),
                new CombatModule(),
                new CommandsModule()
            );
        } catch(Exception e) {
            e.printStackTrace();
            this.onDisable();
            return;
        }

        this.guiceInjector.getInstance(PluginLogger.class).debug("Guice injector created.");

        // Post 'enable' event
        this.guiceInjector.getInstance(PluginEventsExecutive.class).postEnableEvent();
        this.guiceInjector.getInstance(PluginLogger.class).debug("PluginEnableEvent posted, plugin is fully enabled.");
    }

    @Override
    public void onDisable() {
        // If Guice injector is created and available
        if(this.guiceInjector != null) {
            // Post 'disable' event
            this.guiceInjector.getInstance(PluginEventsExecutive.class).postDisableEvent();
            this.guiceInjector.getInstance(PluginLogger.class).debug("PluginDisableEvent posted, plugin is fully disabled.");
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