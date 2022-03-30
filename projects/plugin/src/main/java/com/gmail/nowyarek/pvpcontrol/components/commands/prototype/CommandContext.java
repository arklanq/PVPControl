package com.gmail.nowyarek.pvpcontrol.components.commands.prototype;

import org.bukkit.command.Command;

public class CommandContext {
    private final org.bukkit.command.Command bukkitRootCommand;
    private final String usedRootCommandLabel;

    public CommandContext(Command bukkitRootCommand, String usedRootCommandLabel) {
        this.bukkitRootCommand = bukkitRootCommand;
        this.usedRootCommandLabel = usedRootCommandLabel;
    }

    public Command getBukkitRootCommand() {
        return bukkitRootCommand;
    }

    public String getUsedRootCommandLabel() {
        return usedRootCommandLabel;
    }
}
