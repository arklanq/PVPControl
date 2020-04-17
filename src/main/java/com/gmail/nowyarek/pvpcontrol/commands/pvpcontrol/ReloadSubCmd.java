package com.gmail.nowyarek.pvpcontrol.commands.pvpcontrol;

import com.gmail.nowyarek.pvpcontrol.PVPControl;
import com.gmail.nowyarek.pvpcontrol.commands.StructuralSubCommand;
import com.gmail.nowyarek.pvpcontrol.io.Localization;
import com.gmail.nowyarek.pvpcontrol.io.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadSubCmd extends StructuralSubCommand {

    public ReloadSubCmd(PVPControl plugin) {
        super(
            plugin, "reload", "pvpcontrol.cmd.pvpcontrol.reload",
            Localization.translate(Text.SUB_COMMAND_RELOAD_USAGE),
            null
        );
    }

    @Override
    public boolean onCommandTrigger(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        boolean cmdFulfilled = super.onCommandTrigger(sender, command, label, args);
        if(!cmdFulfilled) {
            plugin.onReload();
            if(!(sender instanceof ConsoleCommandSender)) {
                plugin.getConsole().annoucement(Text.RELOAD_COMPLETE);
            }
            getMessageRecipient(sender).annoucement(Text.SUB_COMMAND_RELOAD_RELOADED_SUCCESSFULLY);
        }
        return true;
    }
}
