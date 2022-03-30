package com.gmail.nowyarek.pvpcontrol.components.commands.prototype;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Collections;
import java.util.List;

public abstract class RootCommand extends Command implements CommandExecutor, TabCompleter {

    public RootCommand(String name) {
        super(name);
    }
    public RootCommand(String name, String description) {
        super(name, description);
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command bukkitCommand, String label, String[] args) {
        CommandContext commandContext = new CommandContext(bukkitCommand, label);

        try {
            this.parseCommandEvent(sender, args, commandContext);
            return true;
        } catch(Exception e) {
            sender.sendMessage(Command.defaults.errorMessage.get());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command bukkitCommand, String label, String[] args) {
        CommandContext commandContext = new CommandContext(bukkitCommand, label);

        try {
            return this.parseTabCompleteEvent(sender, args, commandContext);
        } catch(Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

}
