package com.gmail.nowyarek.pvpcontrol.components.commands.prototype;

import com.gmail.nowyarek.pvpcontrol.utils.Property;
import org.bukkit.ChatColor;

import jakarta.inject.Provider;

public class CommandDefaults {
    public Provider<String> errorMessage = new Property<>(ChatColor.RED + "There was an error while processing your last command.");
    public Provider<String> missingPermissionsMessage = new Property<>(ChatColor.RED + "There was an error while processing your last command.");
}
