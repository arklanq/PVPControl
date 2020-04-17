package com.gmail.nowyarek.pvpcontrol.commands.pvpcontrol;

import com.gmail.nowyarek.pvpcontrol.PVPControl;
import com.gmail.nowyarek.pvpcontrol.commands.StructuralRootCommand;
import com.gmail.nowyarek.pvpcontrol.commands.StructuralSubCommand;
import com.gmail.nowyarek.pvpcontrol.core.PvpPlayersStore;
import com.gmail.nowyarek.pvpcontrol.io.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PvpControlCmd extends StructuralRootCommand {
    private final PvpPlayersStore pvpStore;

    public PvpControlCmd(PVPControl plugin) {
        super(
            plugin, "pvpcontrol", "pvpcontrol.cmd.pvpcontrol",
            Localization.translate(Text.COMMAND_PVPCONTROL_USAGE),
            new StructuralSubCommand[] {
                new HelpSubCmd(plugin),
                new ReloadSubCmd(plugin),
            }
        );
        pvpStore = plugin.getPlayersStore();
    }

    @Override
    public boolean onCommandTrigger(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        boolean cmdFulfilled = super.onCommandTrigger(sender, command, label, args);
        if(!cmdFulfilled) {
            if(args.length == 0) {
               StructuralSubCommand helpSubCmd = Objects.requireNonNull(this.getSubCommandByName("help"));
                helpSubCmd.onCommandTrigger(sender, command, label, args);
            } else {
                getMessageRecipient(sender).warning(Text.INCORRECT_COMMAND_SYNTAX, variables);
            }
            return true;
        } else {
            return false;
        }
    }
}
