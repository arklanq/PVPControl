package com.gmail.nowyarek.pvpcontrol.commands;

import com.gmail.nowyarek.pvpcontrol.PVPControl;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class StructuralRootCommand extends StructuralSubCommand implements CommandExecutor {
    protected PluginCommand bukkitCommand;

    public StructuralRootCommand(
            @NotNull PVPControl plugin,
            @NotNull String name,
            @Nullable String permission,
            @Nullable String usage,
            @Nullable StructuralSubCommand[] subCommands
    ) {
        super(plugin, name, permission, usage, subCommands);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return this.onCommandTrigger(sender, command, label, args);
    }

    public void register() {
        Objects.requireNonNull(plugin.getCommand(name)).setExecutor(this);
        bukkitCommand = plugin.getCommand(name);
    }

    public void unregister() {
        if(bukkitCommand != null) {
            bukkitCommand.setExecutor(null); // Should work lol
            bukkitCommand = null;
        }
    }

}
