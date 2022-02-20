package com.gmail.nowyarek.pvpcontrol.components.l10n;

import com.gmail.nowyarek.pvpcontrol.PvPControlPlugin;
import com.gmail.nowyarek.pvpcontrol.annotations.Blocking;
import com.gmail.nowyarek.pvpcontrol.components.logging.PluginLogger;
import com.gmail.nowyarek.pvpcontrol.components.resources.ResourceBundleCompletenessValidator;

import javax.inject.Inject;
import javax.inject.Provider;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class LanguagesManager {
    private static final DecimalFormat percentageFormat = new DecimalFormat("00.00");
    private final PvPControlPlugin plugin;
    private final PluginLogger logger;
    private final Localization localization;
    private final String languageCode;
    private final LanguagesDetector languagesDetector;
    private final Provider<Optional<ResourceBundle>> externalRB;
    private final Provider<Optional<ResourceBundle>> internalRB;
    private final Provider<ResourceBundle> defaultRB;

    private List<String> externalLangs, internalLangs;

    @Inject
    @Blocking
    LanguagesManager(
        PvPControlPlugin plugin,
        PluginLogger logger,
        Localization localization,
        @LanguageCode String languageCode,
        LanguagesDetector languagesDetector,
        @ExternalLangResourceBundle Provider<Optional<ResourceBundle>> externalRB,
        @InternalLangResourceBundle Provider<Optional<ResourceBundle>> internalRB,
        @DefaultLangResourceBundle Provider<ResourceBundle> defaultRB
    ) {
        this.plugin = plugin;
        this.logger = logger;
        this.localization = localization;
        this.languageCode = languageCode;
        this.languagesDetector = languagesDetector;
        this.externalRB = externalRB;
        this.internalRB = internalRB;
        this.defaultRB = defaultRB;

        this.initializeSync();
    }

    public CompletableFuture<Void> reinitialize() {
        return initializeAsync();
    }

    private CompletableFuture<Void> initializeAsync() {
        return CompletableFuture.runAsync(() -> {
            try {
                this.externalLangs = this.languagesDetector.detectExternalLanguages().get();
                this.internalLangs = this.languagesDetector.detectInternalLanguages().get();

                ResourceBundle defaultTranslations = defaultRB.get();
                Optional<ResourceBundle> externalTranslations = externalRB.get();
                Optional<ResourceBundle> internalTranslations = internalRB.get();

                if (externalTranslations.isPresent()) {
                    double completeness = ResourceBundleCompletenessValidator.checkCompletness(defaultTranslations, externalTranslations.get());
                    if (completeness == 100)
                        this.logger.info(
                            this.localization.builder("localization.using_custom_translations")
                                .addVariable("%languageCode%", this.languageCode)
                                .toString()
                        );
                    else
                        this.logger.warn(
                            this.localization.builder("localization.using_custom_incomplete_translations")
                                .addVariable("%languageCode%", this.languageCode)
                                .addVariable("%completeness%", percentageFormat.format(completeness))
                                .addVariable("%link%", "https://blabla")
                                .toString()
                        );
                } else if (internalTranslations.isPresent()) {
                    double completeness = ResourceBundleCompletenessValidator.checkCompletness(defaultTranslations, internalTranslations.get());
                    if (completeness < 100)
                        this.logger.warn(
                            this.localization.builder("localization.using_built_in_incomplete_translations")
                                .addVariable("%languageCode%", this.languageCode)
                                .addVariable("%completeness%", percentageFormat.format(completeness))
                                .addVariable("%link%", "https://blabla")
                                .toString()
                        );
                } else {
                    this.logger.warn(
                        this.localization.builder("localization.lanaguge_not_supported")
                            .addVariable("%language%", this.languageCode)
                            .addVariable("%built_in_languages%", String.join(", ", internalLangs))
                            .addVariable("%link%", "https://blabla")
                            .toString()
                    );
                }
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        });
    }

    @Blocking
    private void initializeSync() {
        try {
            this.initializeAsync().get();
        } catch (Exception e) {
            e.printStackTrace();
            this.plugin.onDisable();
        }
    }

    public List<String> getAvailableExternalLanguages() {
        return externalLangs;
    }

    public List<String> getAvailableInternalLanguages() {
        return internalLangs;
    }
}
