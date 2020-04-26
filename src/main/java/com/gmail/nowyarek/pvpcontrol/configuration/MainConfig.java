package com.gmail.nowyarek.pvpcontrol.configuration;

import com.gmail.nowyarek.pvpcontrol.PVPControl;
import com.gmail.nowyarek.pvpcontrol.io.Console;
import com.gmail.nowyarek.pvpcontrol.io.Text;
import com.gmail.nowyarek.pvpcontrol.io.Variables;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class MainConfig extends ConfigWithDefaults {
    public Content content;

    public MainConfig(PVPControl plugin) {
        super(plugin, "config.yml");
    }

    @Override
    public void initialize() {
        try {
            super.initialize();
            if(oldConfiguration) {
                plugin.getConsole().warning(Text.OLD_CONFIG, new Variables("%file%", configName));
            }
        } catch(InvalidConfigurationException e) {
            this.initializeDefaults();
            configuration = defaultsConfiguration;
            plugin.getConsole().error(Text.CORRUPTED_CONFIG, new Variables("%file%", "config.yml"));
        }
        verifyContent();
    }

    @Override
    public void reload() {
        this.initialize();
    }

    private void verifyContent() {
        content = new Content(plugin.getConsole(), configuration, defaultsConfiguration);
    }


    public static class Content {
        private static Console console;
        private static FileConfiguration configuration;
        private static FileConfiguration defaults;
        private static Variables sharedVariables;
        public final General General;
        public final PVP PVP;
        public final Hooks Hooks;

        public Content(Console console, FileConfiguration configuration, FileConfiguration defaults) {
            Content.console = console; Content.configuration = configuration; Content.defaults = defaults;
            Content.sharedVariables = new Variables("%file%", "config.yml");
            General = new General(configuration, defaults);
            PVP = new PVP(configuration, defaults);
            Hooks = new Hooks(configuration, defaults);
        }

        private static int verifyIntField(@NotNull String fieldPath, boolean unsigned) {
            if(configuration.isInt(fieldPath)) {
                int value = configuration.getInt(fieldPath);
                if(value < 0 && unsigned) {
                    value = defaults.getInt(fieldPath);
                    Variables var = sharedVariables.clone();
                    var.addVariable("%position%", fieldPath);
                    var.addVariable("%expected%", "positive whole number");
                    var.addVariable("%defaultValue%", String.valueOf(value));
                    console.warning(Text.INVALID_ENTRY, var);
                }
                return value;
            } else {
                int value = defaults.getInt(fieldPath);
                Variables var = sharedVariables.clone();
                var.addVariable("%position%", fieldPath);
                var.addVariable("%expected%", unsigned ? "positive whole number" : "whole number");
                var.addVariable("%defaultValue%", String.valueOf(value));
                console.warning(Text.INVALID_ENTRY, var);
                return value;
            }
        }
        private static boolean verifyBooleanField(@NotNull String fieldPath) {
            if(configuration.isBoolean(fieldPath)) {
                return configuration.getBoolean(fieldPath);
            } else {
                boolean value = defaults.getBoolean(fieldPath);
                Variables var = sharedVariables.clone();
                var.addVariable("%position%", fieldPath);
                var.addVariable("%expected%", "true / false");
                var.addVariable("%defaultValue%", String.valueOf(value));
                console.warning(Text.INVALID_ENTRY, var);
                return value;
            }
        }
        private static List<String> verifyStringListField(@NotNull String fieldPath) {
            if(configuration.isList(fieldPath)) {
                return configuration.getStringList(fieldPath);
            } else {
                List<String> value = defaults.getStringList(fieldPath);
                Variables var = sharedVariables.clone();
                var.addVariable("%position%", fieldPath);
                var.addVariable("%expected%", "texts list");
                var.addVariable("%defaultValue%", String.format("[%s]", String.join(", ", value)));
                console.warning(Text.INVALID_ENTRY, var);
                return value;
            }
        }

        public static class General {
            public final String Language;

            public General(FileConfiguration configuration, FileConfiguration defaults) {
                Language = configuration.getString("General.Language");
            }
        }

        public static class PVP {
            public final int TimeInPVP;
            public final boolean TurnOffFlyOnPVP;
            public final boolean DisableInvisibilityOnPVP;
            public final boolean BlockAllCommandsOnPVP;
            public final boolean BlockAnyKindOfTeleportOnPVP;
            public final boolean BlockChorusFruitTeleport;
            public final boolean BlockEnderPeralTeleport;
            public final boolean KillOnLogoutDuringPVP;
            public final boolean DropItemsOnKill;
            public final boolean BroadcastPlayerLoggedOutOnPVP;
            public final List<String> CommandsExecutedWhenLogoutInPVP;
            public final List<String> BlockOnlySelectedCommandsOnPVP;

            public PVP(FileConfiguration configuration, FileConfiguration defaults) {
                TimeInPVP = verifyIntField("PVP.TimeInPVP", true);
                TurnOffFlyOnPVP = verifyBooleanField("PVP.TurnOffFlyOnPVP");
                DisableInvisibilityOnPVP = verifyBooleanField("PVP.DisableInvisibilityOnPVP");
                BlockAllCommandsOnPVP = verifyBooleanField("PVP.BlockAllCommandsOnPVP");
                BlockAnyKindOfTeleportOnPVP = verifyBooleanField("PVP.BlockAnyKindOfTeleportOnPVP");
                BlockChorusFruitTeleport = verifyBooleanField("PVP.BlockChorusFruitTeleport");
                BlockEnderPeralTeleport = verifyBooleanField("PVP.BlockEnderPeralTeleport");
                KillOnLogoutDuringPVP = verifyBooleanField("PVP.KillOnLogoutDuringPVP");
                DropItemsOnKill = verifyBooleanField("PVP.DropItemsOnKill");
                BroadcastPlayerLoggedOutOnPVP = verifyBooleanField("PVP.BroadcastPlayerLoggedOutOnPVP");
                CommandsExecutedWhenLogoutInPVP = verifyStringListField("PVP.CommandsExecutedWhenLogoutInPVP");
                BlockOnlySelectedCommandsOnPVP = verifyStringListField("PVP.BlockOnlySelectedCommandsOnPVP");
            }
        }

        public static class Hooks {
            public final boolean DisableEssentialsGodModeOnHit;

            public Hooks(FileConfiguration configuration, FileConfiguration defaults) {
                DisableEssentialsGodModeOnHit = verifyBooleanField("Hooks.DisableEssentialsGodModeOnHit");
            }
        }

    }

}
