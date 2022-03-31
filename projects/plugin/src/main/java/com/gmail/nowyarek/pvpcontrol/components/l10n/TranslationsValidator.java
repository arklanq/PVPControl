package com.gmail.nowyarek.pvpcontrol.components.l10n;

import com.gmail.nowyarek.pvpcontrol.components.logging.PluginLogger;
import com.gmail.nowyarek.pvpcontrol.components.resources.ResourceBundleCompletenessValidator;

import javax.inject.Inject;
import javax.inject.Provider;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class TranslationsValidator {
    private static final DecimalFormat percentageFormat = new DecimalFormat("00.00");
    private final PluginLogger logger;
    private final Localization localization;
    private final LanguagesDetector languagesDetector;
    private final TranslationsSuppliersExecutive suppliersExecutive;
    private final String defaultLangCode;
    private final Provider<String> langCodeProvider;

    @Inject
    TranslationsValidator(
        PluginLogger logger,
        Localization localization,
        LanguagesDetector languagesDetector,
        TranslationsSuppliersExecutive suppliersExecutive,
        @DefaultLanguageCode String defaultLangCode,
        @LanguageCode Provider<String> langCodeProvider
    ) {
        this.logger = logger;
        this.localization = localization;
        this.languagesDetector = languagesDetector;
        this.suppliersExecutive = suppliersExecutive;
        this.defaultLangCode = defaultLangCode;
        this.langCodeProvider = langCodeProvider;
    }

    CompletableFuture<Void> validateAsync() {
        return CompletableFuture.runAsync(() -> {
            // Find out current language code
            String langCode = this.langCodeProvider.get();

            // Get fallback translations supplier
            TranslationsSupplier fallbackSupplier = (
                langCode.equalsIgnoreCase(this.defaultLangCode)
                    ? findTranslationsSupplier(ExternalTranslationsSupplier.class)
                    : findTranslationsSupplier(FallbackTranslationsSupplier.class)
            ).orElseThrow(
                () -> new IllegalStateException(String.format("Not found valid fallback translations supplier for language `%s`.", langCode))
            );

            // Validate ExternalTranslationsSupplier
            Optional<TranslationsSupplier> supplier;

            if ((supplier = findTranslationsSupplier(ExternalTranslationsSupplier.class)).isPresent() && supplier.get().isAvailable()) {
                double completeness = ResourceBundleCompletenessValidator.checkCompletness(
                    Objects.requireNonNull(fallbackSupplier.getResourceBundle()),
                    Objects.requireNonNull(supplier.get().getResourceBundle())
                );

                if (completeness == 100)
                    this.logger.info(
                        this.localization.builder("localization.using_custom_translations")
                            .addVariable("%languageCode%", langCode)
                            .toString()
                    );
                else
                    this.logger.warn(
                        this.localization.builder("localization.using_custom_incomplete_translations")
                            .addVariable("%languageCode%", langCode)
                            .addVariable("%completeness%", percentageFormat.format(completeness))
                            .addVariable("%link%", "https://blabla")
                            .toString()
                    );
            } else if ((supplier = findTranslationsSupplier(InternalTranslationsSupplier.class)).isPresent() && supplier.get().isAvailable()) {
                double completeness = ResourceBundleCompletenessValidator.checkCompletness(
                    Objects.requireNonNull(fallbackSupplier.getResourceBundle()),
                    Objects.requireNonNull(supplier.get().getResourceBundle())
                );

                if (completeness < 100)
                    this.logger.info(
                        this.localization.builder("localization.using_built_in_incomplete_translations")
                            .addVariable("%languageCode%", langCode)
                            .addVariable("%completeness%", percentageFormat.format(completeness))
                            .addVariable("%link%", "https://blabla")
                            .toString()
                    );
            } else {
                List<String> availableBuiltInLanguages = this.languagesDetector.detectInternalLanguages().join();

                this.logger.warn(
                    this.localization.builder("localization.lanaguge_not_supported")
                        .addVariable("%language%", langCode)
                        .addVariable("%built_in_languages%", String.join(", ", availableBuiltInLanguages))
                        .addVariable("%link%", "https://blabla")
                        .toString()
                );
            }
        });
    }

    private Optional<TranslationsSupplier> findTranslationsSupplier(Class<? extends TranslationsSupplier> clazz) {
        return this.suppliersExecutive.getSuppliers().stream()
            .filter(clazz::isInstance)
            .findFirst();
    }

}
