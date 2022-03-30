package com.gmail.nowyarek.pvpcontrol.components.settings;

import com.gmail.nowyarek.pvpcontrol.components.configuration.ConfigurationValidation;
import com.gmail.nowyarek.pvpcontrol.components.configuration.ViolationMessageBuilder;
import com.gmail.nowyarek.pvpcontrol.utils.ConfigurationSectionUtils;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;

public class BossbarSettings extends AbstractSettingsSection {
    private int combatDuration;
    private boolean enabled;
    private BarStyle style;
    private BarColor color;
    private int idleThreshold;

    BossbarSettings(FileConfiguration config, FileConfiguration defaultConfig) {
        super(config, defaultConfig);
    }

    @Override
    ConfigurationValidation init() {
        this.bootstrap();

        ConfigurationValidation configuration = new ConfigurationValidation(this.config, this.defaultConfig);

        this.enabled = configuration.requireBoolean("Features.Bossbar.enabled");
        this.style = configuration.requireEnum("Features.Bossbar.style", BarStyle.class);
        this.color = configuration.requireEnum("Features.Bossbar.color", BarColor.class);
        this.idleThreshold = configuration.requireInt("Features.Bossbar.idleThreshold");

        if(this.idleThreshold < 0 || this.idleThreshold > this.combatDuration) {
            ViolationMessageBuilder violationBuilder = ViolationMessageBuilder
                .forPath(ConfigurationSectionUtils.joinPath(this.config.getCurrentPath(), "Features.Bossbar.idleThreshold"))
                .expectedType(Integer.class)
                .actualValue(this.idleThreshold)
                .defaultValue(this.defaultConfig.getInt("Features.Bossbar.idleThreshold"))
                .message(String.format("Value at path {path} represents a number out of range (0-%s).", this.combatDuration));

            configuration.addViolation(violationBuilder.toString());
        }

        return configuration;
    }

    private void bootstrap() {
        try {
            this.combatDuration = Integer.parseInt(Objects.requireNonNull(this.config.getString("PvP.combatDuration")));
        } catch(NullPointerException | NumberFormatException e) {
            this.combatDuration = this.defaultConfig.getInt("PvP.combatDuration");
        }
    }

    public boolean isEnabled() {
        return enabled;
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
