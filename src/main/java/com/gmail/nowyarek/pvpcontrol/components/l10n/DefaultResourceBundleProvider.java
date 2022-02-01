package com.gmail.nowyarek.pvpcontrol.components.l10n;

import com.gmail.nowyarek.pvpcontrol.PVPControl;
import com.gmail.nowyarek.pvpcontrol.components.logging.PluginLogger;
import com.gmail.nowyarek.pvpcontrol.components.settings.SettingsProvider;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.google.common.base.Preconditions.checkState;

@Singleton
public class DefaultResourceBundleProvider extends ResourceBundleProvider {
    private final PluginLogger logger;
    private final String defaultLanguageCode;

    @Inject
    public DefaultResourceBundleProvider(
        PVPControl plugin,
        SettingsProvider settingsProvider,
        Provider<ResourceBundleConstructor> resourceBundleConstructorProvider,
        PluginLogger logger,
        @DefaultLanguageCode String defaultLanguageCode
    ) {
        super(plugin, settingsProvider, resourceBundleConstructorProvider);
        this.logger = logger;
        this.defaultLanguageCode = defaultLanguageCode;
        settingsProvider.getEventBus().register(this);
    }

    @Override
    protected Optional<ResourceBundle> createResourceBundle() throws Exception {
        ResourceBundleConstructor constructor = this.resourceBundleConstructorProvider.get();
        Optional<ResourceBundle> resourceBundle = constructor.constructDefaultResourceBundle().get();
        checkState(resourceBundle.isPresent(), String.format("Default translations ResourceBundle (%s) must be available.", defaultLanguageCode));
        logger.debug(String.format("Default translations ResourceBundle (%s) loaded.", defaultLanguageCode));
        return resourceBundle;
    }
}
