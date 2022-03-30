package com.gmail.nowyarek.pvpcontrol.components.commands;

import com.gmail.nowyarek.pvpcontrol.PvPControlPlugin;
import com.gmail.nowyarek.pvpcontrol.components.commands.prototype.Command;
import com.gmail.nowyarek.pvpcontrol.components.commands.prototype.RootCommand;
import com.gmail.nowyarek.pvpcontrol.components.l10n.Localization;
import com.gmail.nowyarek.pvpcontrol.components.plugin.PluginDisableEvent;
import com.gmail.nowyarek.pvpcontrol.components.plugin.PluginEnableEvent;
import com.google.common.eventbus.Subscribe;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.Objects;

@Singleton
class CommandsRegistry {
    private final PvPControlPlugin plugin;
    private final Provider<MainCommand> mainCommandProvider;

    @Inject
    CommandsRegistry(PvPControlPlugin plugin, Provider<MainCommand> mainCommandProvider, Localization localization) {
        this.plugin = plugin;
        this.mainCommandProvider = mainCommandProvider;

        this.setupCommandDefaults(localization);

        plugin.getEventBus().register(this);
    }

    @Subscribe
    void onPluginEnable(PluginEnableEvent e) {
        org.bukkit.command.PluginCommand pluginCommand = Objects.requireNonNull(this.plugin.getCommand("pvpc"));
        RootCommand mainCommand = this.mainCommandProvider.get();
        pluginCommand.setExecutor(mainCommand);
        pluginCommand.setTabCompleter(mainCommand);
    }

    @Subscribe
    void onPluginDisable(PluginDisableEvent e) {
        Objects.requireNonNull(this.plugin.getCommand("pvpc"))
            .setExecutor(null);
    }

    private void setupCommandDefaults(Localization localization) {
        Command.defaults.errorMessage = () -> localization.t("commands.error");
        Command.defaults.missingPermissionsMessage = () -> localization.t("commands.missing_permissions");
    }

}
