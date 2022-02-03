package com.gmail.nowyarek.pvpcontrol.components.l10n;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ResourceBundle;

class ExternalResourceBundleProvider implements Provider<ResourceBundle> {
    private final LangResourceBundlesManager manager;

    @Inject
    ExternalResourceBundleProvider(LangResourceBundlesManager manager) {
        this.manager = manager;
    }

    @Override
    public ResourceBundle get() {
        return this.manager.externalResourceBundle.orElse(null);
    }
}

class InternalResourceBundleProvider implements Provider<ResourceBundle> {
    private final LangResourceBundlesManager manager;

    @Inject
    InternalResourceBundleProvider(LangResourceBundlesManager manager) {
        this.manager = manager;
    }

    @Override
    public ResourceBundle get() {
        return this.manager.internalResourceBundle.orElse(null);
    }
}

class DefaultResourceBundleProvider implements Provider<ResourceBundle> {
    private final LangResourceBundlesManager manager;

    @Inject
    DefaultResourceBundleProvider(LangResourceBundlesManager manager) {
        this.manager = manager;
    }

    @Override
    public ResourceBundle get() {
        return this.manager.defaultResourceBundle.orElse(null);
    }
}
