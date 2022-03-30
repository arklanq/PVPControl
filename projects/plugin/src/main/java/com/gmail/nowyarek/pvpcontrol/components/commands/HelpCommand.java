package com.gmail.nowyarek.pvpcontrol.components.commands;

import com.gmail.nowyarek.pvpcontrol.components.commands.prototype.Command;
import com.gmail.nowyarek.pvpcontrol.components.commands.prototype.CommandContext;
import com.gmail.nowyarek.pvpcontrol.components.commands.prototype.RootCommand;
import com.gmail.nowyarek.pvpcontrol.components.l10n.Localization;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;
import java.util.Objects;

public class HelpCommand extends Command {
    private final Localization localization;

    @Inject
    public HelpCommand(Localization localization) {
        super("help", localization.t("commands.help.description"));
        this.defineRequiredPermision("pvpcontrol.commands.help");
        this.localization = localization;
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

        this.print(sender);
    }

    public void print(CommandSender sender) {
        RootCommand rootCommand = Objects.requireNonNull(this.getRootCommand().orElse(null));

        StringBuilder sb = new StringBuilder();
        sb.append(this.localization.t("commands.help.header")).append(ChatColor.RESET).append("\n");

        rootCommand.getCommands().forEach(
            (Command command) -> sb.append(
                this.localization.builder("commands.help.entry")
                    .addVariable("%command%", command.getName())
                    .addVariable("%arguments%", command.getArguments().map((String[] argumentsArray) -> String.join(" ", argumentsArray)).orElse(""))
                    .addVariable("%description%", command.getDescription().orElse(""))
                    .toString()
            ).append(ChatColor.RESET).append("\n")
        );

        sender.sendMessage(sb.toString());
    }

}
