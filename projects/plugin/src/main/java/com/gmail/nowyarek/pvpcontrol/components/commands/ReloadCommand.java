package com.gmail.nowyarek.pvpcontrol.components.commands;

import co.aikar.taskchain.TaskChainFactory;
import com.gmail.nowyarek.pvpcontrol.components.commands.prototype.Command;
import com.gmail.nowyarek.pvpcontrol.components.commands.prototype.CommandContext;
import com.gmail.nowyarek.pvpcontrol.components.l10n.Localization;
import com.gmail.nowyarek.pvpcontrol.components.l10n.TranslationsManager;
import com.gmail.nowyarek.pvpcontrol.components.settings.SettingsProvider;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import jakarta.inject.Inject;

public class ReloadCommand extends Command {
    private final Localization localization;
    private final SettingsProvider settingsProvider;
    private final TranslationsManager translationsManager;
    private final TaskChainFactory taskChainFactory;
    private final JavaPlugin plugin;

    @Inject
    public ReloadCommand(
        Localization localization,
        SettingsProvider settingsProvider,
        TranslationsManager translationsManager,
        TaskChainFactory taskChainFactory,
        JavaPlugin plugin
    ) {
        super("reload", localization.t("commands.reload.description"));

        this.localization = localization;
        this.settingsProvider = settingsProvider;
        this.translationsManager = translationsManager;
        this.taskChainFactory = taskChainFactory;
        this.plugin = plugin;

        this.defineRequiredPermision("pvpcontrol.commands.reload");
    }

    @Override
    public void onCommand(CommandSender sender, String[] args, CommandContext commandContext) {
        if (args.length > 0) {
            sender.sendMessage(
                this.localization.t("commands.invalid_arguments")
                    + "\n"
                    + this.localization.builder("commands.usage")
                    .addVariable("%command%", this.getNamespace())
                    .addVariable("%description%", this.getDescription().orElse(""))
            );
            return;
        }

        this.taskChainFactory.newChain()
            .async(() -> {
                settingsProvider.reinitialize().join();
                translationsManager.reinitialize().join();
            })
            .sync(() -> {
                String message = this.localization.t("commands.reload.success");

                if (sender instanceof Player && ((Player) sender).isOnline())
                    sender.sendMessage(message);

                this.plugin.getServer().getConsoleSender().sendMessage(message);
            })
            .execute();
    }

}
