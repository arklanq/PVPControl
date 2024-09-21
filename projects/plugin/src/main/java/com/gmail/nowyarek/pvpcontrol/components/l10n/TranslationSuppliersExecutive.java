package com.gmail.nowyarek.pvpcontrol.components.l10n;

import com.gmail.nowyarek.pvpcontrol.components.logging.PluginLogger;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class TranslationSuppliersExecutive {
    private final PluginLogger logger;
    private final Provider<String> langCodeProvider;
    private final String defaultLangCode;
    private final Provider<Set<TranslationSupplier>> tSupplierSetProvider;
    private final Set<TranslationSupplier> activeSuppliers = new LinkedHashSet<>();

    @Inject
    TranslationSuppliersExecutive(
        PluginLogger logger,
        @LanguageCode Provider<String> langCodeProvider,
        @DefaultLanguageCode String defaultLangCode,
        @Named("External") Provider<TranslationSupplier> externalTSProvider,
        @Named("Internal") Provider<TranslationSupplier> internalTSProvider,
        @Named("Fallback") Provider<TranslationSupplier> fallbackTSProvider
    ) {
        this.logger = logger;
        this.langCodeProvider = langCodeProvider;
        this.defaultLangCode = defaultLangCode;
        //TODO: Suppliers should implement Comparable interface and we should be able to sort them based on Priority
        this.tSupplierSetProvider = () -> Stream.of(fallbackTSProvider, internalTSProvider, externalTSProvider).map(Provider::get).collect(Collectors.toSet());
    }

    CompletableFuture<Void> initializeAsync() {
        return CompletableFuture.runAsync(() -> {
            // Find out current language
            String langCode = this.langCodeProvider.get();
            boolean isDefaultLanguage = langCode.equalsIgnoreCase(this.defaultLangCode);
            this.logger.debug(String.format("Selected language `%s`.", langCode.toUpperCase()));

            // Instantiate TranslationSupplier objects
            Set<TranslationSupplier> suppliers = this.tSupplierSetProvider.get();

            /*
             * Filter out (don't initialize) FallbackTranslationSupplier if the lang code is English,
             * because InternalTranslationSupplier and FallbackTranslationSupplier would
             * unncecessary duplicate the same entries from their ResourceBundle
             */
            if (isDefaultLanguage)
                suppliers.removeIf((TranslationSupplier supplier) -> (supplier instanceof FallbackTranslationSupplier));


            // Initialize suppliers and await synchronously for them to finish their work
            CompletableFuture.allOf(
                suppliers.stream()
                    .map(TranslationSupplier::initializeAsync)
                    .toArray(CompletableFuture<?>[]::new)
            ).join();

            // Filter out not available suppliers & collect
            suppliers.removeIf((TranslationSupplier supplier) -> (!supplier.isAvailable()));

            Preconditions.checkState(suppliers.size() > 0, "At least one TranslationSupplier must be available, none detected, even the FallbackTranslationSupplier.");

            // Add suppliers to
            this.activeSuppliers.addAll(suppliers);

            // Announce active suppliers
            suppliers.forEach((TranslationSupplier supplier) -> this.logger.debug(
                String.format("%s (%s) %s", supplier.getClass().getSimpleName(), supplier.getLanguageCode(), supplier.isAvailable() ? "available" : "not available")
            ));
        });
    }

    public ImmutableSet<TranslationSupplier> getActiveSuppliers() {
        return ImmutableSet.copyOf(this.activeSuppliers);
    }

    /**
     * This method lookup in all available TranslationSupplier's for the specified key
     * and returns the translations if found, orherwise throws {@link MissingTranslationException}.
     *
     * @param key - translation key
     * @return translated text
     * @throws MissingTranslationException - in case the translation wasn't found for the specified key
     */
    public String getString(String key) throws MissingTranslationException {
        Optional<String> optionalValue = this.activeSuppliers.stream()
            .map((TranslationSupplier supplier) -> supplier.getString(key))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .findFirst();

        if (optionalValue.isPresent())
            return optionalValue.get();
        else
            throw new MissingTranslationException(key);
    }

}
