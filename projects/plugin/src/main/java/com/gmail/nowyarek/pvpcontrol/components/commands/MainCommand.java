package com.gmail.nowyarek.pvpcontrol.components.commands;

import com.gmail.nowyarek.pvpcontrol.components.commands.prototype.CommandContext;
import com.gmail.nowyarek.pvpcontrol.components.commands.prototype.RootCommand;
import com.gmail.nowyarek.pvpcontrol.components.l10n.Localization;
import com.gmail.nowyarek.pvpcontrol.utils.StringVariable;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;

public class MainCommand extends RootCommand {
    private final Localization localization;

    @Inject
    public MainCommand(
        Localization localization,
        HelpCommand helpCommand,
        ReloadCommand reloadCommand
    ) {
        super("pvpc");
        this.defineRequiredPermision("pvpcontrol.commands.help");
        this.addCommand(helpCommand);
        this.addCommand(reloadCommand);
        this.localization = localization;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args, CommandContext commandContext) {
        if (args.length > 0) {
            sender.sendMessage(
                this.localization.t("commands.not_found", new StringVariable("%command%", args[0]))
            );
            return;
        }

        HelpCommand helpCommand = this.getTypedCommand("help", HelpCommand.class);
        helpCommand.print(sender);
    }
}
