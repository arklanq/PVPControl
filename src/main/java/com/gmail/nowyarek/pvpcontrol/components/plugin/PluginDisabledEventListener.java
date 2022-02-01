package com.gmail.nowyarek.pvpcontrol.components.plugin;

import com.gmail.nowyarek.pvpcontrol.components.l10n.Localization;
import com.gmail.nowyarek.pvpcontrol.components.logging.PluginLogger;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import java.util.EventListener;

public class PluginDisabledEventListener implements EventListener {
    private final PluginLogger logger;
    private final Localization localization;

    @Inject
    public PluginDisabledEventListener(PluginLogger logger, Localization localization) {
        this.logger = logger;
        this.localization = localization;
    }

    @Subscribe
    public void onEvent(PluginDisabledEvent e) {
        logger.info(localization.t("plugin.disabled"));
    }

}
