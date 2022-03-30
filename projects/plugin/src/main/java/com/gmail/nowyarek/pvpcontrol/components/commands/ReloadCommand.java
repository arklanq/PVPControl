package com.gmail.nowyarek.pvpcontrol.components.commands;

import com.gmail.nowyarek.pvpcontrol.components.commands.prototype.Command;
import com.gmail.nowyarek.pvpcontrol.components.commands.prototype.CommandContext;
import com.gmail.nowyarek.pvpcontrol.components.l10n.LangResourceBundlesManager;
import com.gmail.nowyarek.pvpcontrol.components.l10n.LanguagesManager;
import com.gmail.nowyarek.pvpcontrol.components.l10n.Localization;
import com.gmail.nowyarek.pvpcontrol.components.settings.SettingsProvider;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;

public class ReloadCommand extends Command {
    private final Localization localization;
    private final SettingsProvider settingsProvider;
    private final LangResourceBundlesManager langResourceBundlesManager;
    private final LanguagesManager languagesManager;
    private final JavaPlugin plugin;

    @Inject
    public ReloadCommand(
        Localization localization,
        SettingsProvider settingsProvider,
        LangResourceBundlesManager langResourceBundlesManager,
        LanguagesManager languagesManager,
        JavaPlugin plugin
    ) {
        super("reload", localization.t("commands.reload.description"));
        this.defineRequiredPermision("pvpcontrol.commands.reload");
        this.localization = localization;
        this.settingsProvider = settingsProvider;
        this.langResourceBundlesManager = langResourceBundlesManager;
        this.languagesManager = languagesManager;
        this.plugin = plugin;
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

        settingsProvider.reinitialize().join();
        langResourceBundlesManager.reinitialize().join();
        languagesManager.reinitialize().join();

        sender.sendMessage(this.localization.t("commands.reload.success"));

        if(!(sender instanceof ConsoleCommandSender)) {
            this.plugin.getServer().getConsoleSender().sendMessage(this.localization.t("commands.reload.success"));
        }
    }

}
