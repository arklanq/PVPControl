package com.gmail.nowyarek.pvpcontrol.commands.pvpcontrol;

import com.gmail.nowyarek.pvpcontrol.PVPControl;
import com.gmail.nowyarek.pvpcontrol.commands.StructuralSubCommand;
import com.gmail.nowyarek.pvpcontrol.io.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class HelpSubCmd extends StructuralSubCommand {
    private final HelpEntry[][] pagesContent; // command, alias, usage, permission
    private final String[] preformattedPagesContent;

    public HelpSubCmd(PVPControl plugin) {
        super(
            plugin, "help", "pvpcontrol.cmd.pvpcontrol.help",
            Localization.translate(Text.SUB_COMMAND_HELP_USAGE),
            null
        );
        pagesContent = new HelpEntry[][] {
            {
                new HelpEntry("help", "pvpcontrol.cmd.pvpcontrol.help", Text.SUB_COMMAND_HELP_USAGE),
                new HelpEntry("reload", "pvpcontrol.cmd.pvpcontrol.reload", Text.SUB_COMMAND_RELOAD_USAGE),
            },
        };
        preformattedPagesContent = new String[pagesContent.length];
        for(int i=0; i<pagesContent.length; i++) {
            HelpEntry[] entries = pagesContent[i];
            preformattedPagesContent[i] = formatPage(i);
        }
    }

    @Override
    public boolean onCommandTrigger(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        boolean cmdFulfilled = super.onCommandTrigger(sender, command, label, args);
        if(!cmdFulfilled) {
            int pageIndex = 0;
            if(args.length > 0) {
                try {
                    pageIndex = Integer.parseInt(args[0]) - 1;
                } catch(NumberFormatException e) {
                    QueueableMessage message = new QueueableMessage(
                        Text.HELP_INCORRECT_PAGE, new Variables("%usage%", Localization.translate(Text.SUB_COMMAND_HELP_USAGE))
                    );
                    getMessageRecipient(sender).warning(message);
                    return true;
                }

                if(pageIndex < 0 || pageIndex >= pagesContent.length) {
                    QueueableMessage message = new QueueableMessage(
                        Text.HELP_INCORRECT_PAGE,
                        new Variables("%usage%", Localization.translate(Text.SUB_COMMAND_HELP_USAGE))
                    );
                    getMessageRecipient(sender).warning(message);
                    return true;
                }
            }

            sender.sendMessage(preformattedPagesContent[pageIndex]);
        }
        return true;
    }

    private String formatPage(int pageIndex) {
        if(pageIndex < 0 || pageIndex >= pagesContent.length){
            throw new IllegalArgumentException("Invalid page index. Out of bounds.");
        }
        boolean isLastPage = (pageIndex + 1) == pagesContent.length;
        HelpEntry[] pagesEntries = pagesContent[pageIndex];
        StringBuilder page = new StringBuilder();
        page.append(Localization.translate(Text.THEMATIC_BREAK)).append("\n");
        page.append(Localization.translate(Text.HELP_TITLE, new Variables("%page%", String.valueOf(pageIndex + 1)))).append("\n");
        for(HelpEntry pagesEntry : pagesEntries) {
            page.append(formatEntry(pagesEntry)).append("\n");
        }
        if(!isLastPage) {
            page.append(Localization.translate(Text.HELP_NEXT_PAGE_INFO, new Variables("%next_page%", String.valueOf(pageIndex + 2))));
        } else {
            page.append(Localization.translate(Text.HELP_LAST_PAGE_INFO));
        }
        return page.toString();
    }

    private String formatEntry(
            @NotNull HelpEntry helpEntry
    ) {
        Variables var = new Variables();
        var.addVariable("%command%", helpEntry.subcommand);
        var.addVariable("%permission%", helpEntry.permission);
        var.addVariable("%usage%", Localization.translate(helpEntry.usage));
        return Localization.translate(Text.HELP_COMMAND_ENTRY, var);
    }

}

class HelpEntry {
    final String subcommand, permission;
    final Text usage;

    HelpEntry(String subcommand, String permission, Text usage) {
        this.subcommand = subcommand;
        this.permission = permission;
        this.usage = usage;
    }
}
