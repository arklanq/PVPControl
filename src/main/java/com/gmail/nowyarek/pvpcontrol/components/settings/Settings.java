package com.gmail.nowyarek.pvpcontrol.components.settings;

import com.gmail.nowyarek.pvpcontrol.PVPControl;
import com.gmail.nowyarek.pvpcontrol.components.configuration.ConfigFactory;
import com.gmail.nowyarek.pvpcontrol.components.configuration.ConfigInitializationException;
import com.gmail.nowyarek.pvpcontrol.components.configuration.ConfigWithDefaults;
import com.gmail.nowyarek.pvpcontrol.components.plugin.PluginEnableEvent;
import com.google.common.eventbus.Subscribe;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class Settings {
    private final ConfigWithDefaults config;
    public GeneralSettings General;
    public PVPSettings PVP;
    public CommandsSettings Commands;
    public IntegrationsSettings Integrations;
    public ViolationsProcessorFactory violationsProcessorFactory;

    @Inject
    Settings(PVPControl plugin, ConfigFactory configFactory, ViolationsProcessorFactory violationsProcessorFactory) {
        this.config = configFactory.createConfigWithDefaults("settings.yml");
        this.violationsProcessorFactory = violationsProcessorFactory;
        plugin.getEventBus().register(this);
    }

    @Subscribe
    void onPluginEnable(PluginEnableEvent ignoredE) throws ConfigInitializationException {
        this.reload();
    }

    public void reload() throws ConfigInitializationException {
        try {
            this.config.initialize().get();
            this.initializeSections();
        } catch (Exception ex) {
            throw new ConfigInitializationException(this.config.fileName, ex);
        }
    }

    private void initializeSections() {
        this.General = new GeneralSettings(config.configuration, config.defaultsConfiguration);
        this.PVP = new PVPSettings(config.configuration, config.defaultsConfiguration);
        this.Commands = new CommandsSettings(config.configuration, config.defaultsConfiguration);
        this.Integrations = new IntegrationsSettings(config.configuration, config.defaultsConfiguration);

        List<String> violations = Stream.of(General, PVP, Commands, Integrations)
            .map((SettingsSection section) -> section.init().getViolations())
            .flatMap(Collection::stream)
            .collect(Collectors.toList());

        ViolationsProcessor violationsProcessor = this.violationsProcessorFactory.create(
            this.General.getConfigVersion(),
            this.config.defaultsConfiguration.getInt("General.configVersion")
        );
        violationsProcessor.processViolations(violations);
    }



}
