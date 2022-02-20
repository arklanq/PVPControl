package com.gmail.nowyarek.pvpcontrol.components.settings;

public class SettingsLoadEvent {
    private final Settings settings;

    public SettingsLoadEvent(Settings settings) {
        this.settings = settings;
    }

    public Settings getSettings() {
        return settings;
    }
}
