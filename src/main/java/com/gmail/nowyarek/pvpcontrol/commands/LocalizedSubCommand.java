package com.gmail.nowyarek.pvpcontrol.commands;

import com.gmail.nowyarek.pvpcontrol.PVPControl;
import com.gmail.nowyarek.pvpcontrol.io.Localization;
import com.gmail.nowyarek.pvpcontrol.io.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LocalizedSubCommand extends StructuralSubCommand {

    public LocalizedSubCommand(
        @NotNull PVPControl plugin,
        @NotNull String name,
        @Nullable String permission,
        @Nullable Text usage,
        @Nullable StructuralSubCommand[] subCommands
    ) {
        super(
            plugin, name, permission,
            usage != null ? Localization.translate(usage) : null,
            subCommands
        );
    }

}
