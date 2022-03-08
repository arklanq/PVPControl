package com.gmail.nowyarek.pvpcontrol.components.settings;

import com.gmail.nowyarek.pvpcontrol.components.configuration.ConfigurationValidation;
import com.gmail.nowyarek.pvpcontrol.components.configuration.ViolationMessageBuilder;
import com.gmail.nowyarek.pvpcontrol.utils.ConfigurationSectionUtils;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.stream.Collectors;

public class BossbarSettings extends AbstractSettingsSection {
    private final int combatDuration;
    private BarStyle style;
    private BarColor color;
    private int idleThreshold;

    BossbarSettings(FileConfiguration config, FileConfiguration defaultConfig, int combatDuration) {
        super(config, defaultConfig);
        this.combatDuration = combatDuration;
    }

    @Override
    ConfigurationValidation init() {
        ConfigurationValidation configuration = new ConfigurationValidation(this.config, this.defaultConfig);

        this.style = configuration.requireEnum("PvP.Bossbar.style", BarStyle.class);
        this.color = configuration.requireEnum("PvP.Bossbar.color", BarColor.class);
        this.idleThreshold = configuration.requireInt("PvP.Bossbar.idleThreshold");

        if(this.idleThreshold < 0 || this.idleThreshold > this.combatDuration) {
            ViolationMessageBuilder violationBuilder = ViolationMessageBuilder
                .forPath(ConfigurationSectionUtils.joinPath(this.config.getCurrentPath(), "PvP.Bossbar.idleThreshold"))
                .expectedType(Integer.class)
                .actualValue(this.idleThreshold)
                .defaultValue(this.defaultConfig.getInt("PvP.Bossbar.idleThreshold"))
                .message(String.format("Value at path {path} represents a number out of range (0-%s).", this.combatDuration));

            configuration.addViolation(violationBuilder.toString());
        }

        return configuration;
    }

    public BarStyle getStyle() {
        return style;
    }

    public BarColor getColor() {
        return color;
    }

    public int getIdleThreshold() {
        return idleThreshold;
    }
}
