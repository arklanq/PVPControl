package com.gmail.nowyarek.pvpcontrol.commands;

import com.gmail.nowyarek.pvpcontrol.PVPControl;
import com.gmail.nowyarek.pvpcontrol.core.PvpPlayer;
import com.gmail.nowyarek.pvpcontrol.io.MessagesSender;
import com.gmail.nowyarek.pvpcontrol.io.Text;
import com.gmail.nowyarek.pvpcontrol.io.Variables;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StructuralSubCommand {
    @NotNull
    protected final PVPControl plugin;
    @NotNull
    protected final String name, usage;
    @Nullable
    protected final String permission;
    @NotNull
    protected final Variables variables;
    @NotNull
    protected final List<StructuralSubCommand> subCommands;
    @Nullable
    protected StructuralSubCommand parentCommand;
    protected boolean consoleAllowed = true;

    public StructuralSubCommand(
            @NotNull PVPControl plugin,
            @NotNull String name,
            @Nullable String permission,
            @Nullable String usage,
            @Nullable StructuralSubCommand[] subCommands
    ) {
        this.plugin = plugin;
        this.name = name.toLowerCase(); // for sure
        this.permission = permission != null ? permission.toLowerCase() : null; // for sure
        this.usage = usage != null ? usage : "";
        variables = new Variables();
        variables.addVariable("%command%", getFullCommandQualifier());
        variables.addVariable("%usage%", usage);
        variables.addVariable("%permission%", permission != null ? permission : "");

        if(subCommands == null) {
            this.subCommands = new ArrayList<>();
        } else {
            this.subCommands = Arrays.asList(subCommands);
            for(StructuralSubCommand subCommand : this.subCommands) {
                subCommand.parentCommand = this;
            }
        }
    }

    @Nullable
    protected StructuralSubCommand getParentCommand() {
        return parentCommand;
    }

    protected void addSubCommand(@NotNull StructuralSubCommand subCommand) {
        if(!subCommands.contains(subCommand)) {
            subCommand.parentCommand = this;
            subCommands.add(subCommand);
        }
    }

    protected void removeSubCommand(@NotNull StructuralSubCommand subCommand) {
        subCommand.parentCommand = null;
        subCommands.remove(subCommand);
    }

    @Nullable
    protected StructuralSubCommand getSubCommandByName(@NotNull String name) {
        for(StructuralSubCommand subCommand : subCommands) {
            if(subCommand.name.equalsIgnoreCase(name)) {
                return subCommand;
            }
        }
        return null;
    }

    @NotNull
    protected MessagesSender getMessageRecipient(@NotNull CommandSender sender) {
        MessagesSender recipient;
        if(sender instanceof Player) {
            return PvpPlayer.getFromMeta((Player) sender);
        } else {
            return plugin.getConsole();
        }
    }

    protected void setConsoleAllowed(boolean consoleAllowed) {
        this.consoleAllowed = consoleAllowed;
    }

    @NotNull
    protected String getFullCommandQualifier() {
        StringBuilder cmdQualifier = new StringBuilder(name);
        StructuralSubCommand cmd = this;

        while(cmd.getParentCommand() != null) {
            cmdQualifier.insert(0, cmd.getParentCommand().name + " ");
            cmd = cmd.getParentCommand();
        }

        return cmdQualifier.toString();
    }

    public boolean onCommandTrigger(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof ConsoleCommandSender && !consoleAllowed) {
            getMessageRecipient(sender).warning(Text.COMMAND_NOT_ALLOWED_FOR_CONSOLE, variables);
            return true;
        }
        if(permission != null && !sender.hasPermission(permission)) {
            getMessageRecipient(sender).warning(Text.COMMAND_NO_PERMISSIONS, variables);
            return true;
        }
        if(args.length > 0) {
            String subCommandName = args[0].toLowerCase();
            for(StructuralSubCommand subCommand : subCommands) {
                if(subCommand.name.equals(subCommandName)) {
                    String[] argsForSubCommand = Arrays.copyOfRange(args, 1, args.length);
                    subCommand.onCommandTrigger(sender, command, label, argsForSubCommand);
                    return true;
                }
            }
        }
        return false;
    }

}
