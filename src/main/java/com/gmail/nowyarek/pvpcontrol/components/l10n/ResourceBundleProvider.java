package com.gmail.nowyarek.pvpcontrol.components.l10n;

import com.gmail.nowyarek.pvpcontrol.PVPControl;
import com.gmail.nowyarek.pvpcontrol.annotations.Blocking;
import com.gmail.nowyarek.pvpcontrol.components.settings.SettingsLoadEvent;
import com.gmail.nowyarek.pvpcontrol.components.settings.SettingsProvider;
import com.google.common.eventbus.Subscribe;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.EventListener;
import java.util.Optional;
import java.util.ResourceBundle;

abstract class ResourceBundleProvider implements Provider<ResourceBundle>, EventListener {
    private final PVPControl plugin;
    protected final Provider<ResourceBundleConstructor> resourceBundleConstructorProvider;
    protected Optional<ResourceBundle> resourceBundle = Optional.empty();

    @Inject
    ResourceBundleProvider(
        PVPControl plugin,
        SettingsProvider settingsProvider,
        Provider<ResourceBundleConstructor> resourceBundleConstructorProvider
    ) {
        this.plugin = plugin;
        this.resourceBundleConstructorProvider = resourceBundleConstructorProvider;

        settingsProvider.getEventBus().register(this);
    }

    @Override
    @Nullable
    public ResourceBundle get() {
        return resourceBundle.orElse(null);
    }

    @Subscribe
    @Blocking
    void onSettingsLoad(SettingsLoadEvent ignoredEvent) {
        try {
            this.resourceBundle = this.createResourceBundle();
        } catch (Exception e) {
            e.printStackTrace();
            this.plugin.onDisable();
        }
    }

    protected abstract Optional<ResourceBundle> createResourceBundle() throws Exception;
}
