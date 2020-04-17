package com.gmail.nowyarek.pvpcontrol.commands;

import com.gmail.nowyarek.pvpcontrol.PVPControl;
import com.gmail.nowyarek.pvpcontrol.commands.pvpcontrol.PvpControlCmd;

public class CommandsRegistry {
    private final PVPControl plugin;
    private StructuralRootCommand[] rootCommands;

    public CommandsRegistry(PVPControl plugin) {
        this.plugin = plugin;
        initializeRootCommands();
    }

    public void registerCommands() {
        for(StructuralRootCommand rootCmd : rootCommands) {
            rootCmd.register();
        }
    }

    public void reloadCommands() {
        //unregisterCommands();
        initializeRootCommands();
        registerCommands();
    }

    public void unregisterCommands() {
        for(StructuralRootCommand rootCmd : rootCommands) {
            rootCmd.unregister();
        }
    }

    private void initializeRootCommands() {
        rootCommands = new StructuralRootCommand[] {
            new PvpControlCmd(plugin),
        };
    }

}
