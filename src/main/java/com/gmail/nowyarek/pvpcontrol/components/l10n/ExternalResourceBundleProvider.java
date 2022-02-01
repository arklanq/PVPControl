package com.gmail.nowyarek.pvpcontrol.components.l10n;

import com.gmail.nowyarek.pvpcontrol.PVPControl;
import com.gmail.nowyarek.pvpcontrol.components.logging.PluginLogger;
import com.gmail.nowyarek.pvpcontrol.components.settings.SettingsProvider;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.Optional;
import java.util.ResourceBundle;

@Singleton
public class ExternalResourceBundleProvider extends ResourceBundleProvider {
    private final PluginLogger logger;
    private final Provider<String> languageCodeProvider;

    @Inject
    public ExternalResourceBundleProvider(
        PVPControl plugin,
        SettingsProvider settingsProvider,
        Provider<ResourceBundleConstructor> resourceBundleConstructorProvider,
        PluginLogger logger,
        @LanguageCode Provider<String> languageCodeProvider
    ) {
        super(plugin, settingsProvider, resourceBundleConstructorProvider);
        this.logger = logger;
        this.languageCodeProvider = languageCodeProvider;
        settingsProvider.getEventBus().register(this);
    }

    @Override
    protected Optional<ResourceBundle> createResourceBundle() throws Exception {
        ResourceBundleConstructor constructor = this.resourceBundleConstructorProvider.get();
        Optional<ResourceBundle> resourceBundle = Optional.empty();
        try {
            return resourceBundle = constructor.constructExternalResourceBundle().get();
        } finally {
            this.logger.debug(String.format("External translations ResourceBundle (%s) %s.", languageCodeProvider.get(), resourceBundle.isPresent() ? "loaded" : "not available"));
        }
    }
}
