package com.gmail.nowyarek.pvpcontrol.components.plugin;

import com.gmail.nowyarek.pvpcontrol.components.l10n.Localization;
import com.gmail.nowyarek.pvpcontrol.components.logging.PluginLogger;
import com.gmail.nowyarek.pvpcontrol.utils.StringVariable;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Stage;

import java.util.EventListener;

public class PluginEnableEventListener implements EventListener {
    private final Stage stage;
    private final PluginLogger logger;
    private final Localization localization;

    @Inject
    public PluginEnableEventListener(Stage stage, PluginLogger logger, Localization localization) {
        this.stage = stage;
        this.logger = logger;
        this.localization = localization;
    }

    @Subscribe
    public void onEvent(PluginEnableEvent e) {
        logger.info(localization.t("plugin.enabled", new StringVariable("%mode%", stage.toString())));
    }

}
