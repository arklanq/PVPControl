package com.gmail.nowyarek.pvpcontrol.components.l10n;

import com.gmail.nowyarek.pvpcontrol.components.logging.PluginLogger;
import com.google.common.collect.ImmutableList;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class TranslationsSuppliersExecutive {
    private final PluginLogger logger;
    private final Provider<String> langCodeProvider;

    private final Provider<TranslationsSupplier> externalTSProvider;
    private final Provider<TranslationsSupplier> internalTSProvider;
    private final Provider<TranslationsSupplier> fallbackTSProvider;

    private volatile List<TranslationsSupplier> suppliers = new ArrayList<>();

    @Inject
    TranslationsSuppliersExecutive(
        PluginLogger logger,
        @LanguageCode Provider<String> langCodeProvider,
        @Named("External") Provider<TranslationsSupplier> externalTSProvider,
        @Named("Internal") Provider<TranslationsSupplier> internalTSProvider,
        @Named("Fallback") Provider<TranslationsSupplier> fallbackTSProvider
    ) {
        this.logger = logger;
        this.langCodeProvider = langCodeProvider;

        this.externalTSProvider = externalTSProvider;
        this.internalTSProvider = internalTSProvider;
        this.fallbackTSProvider = fallbackTSProvider;
    }

    CompletableFuture<Void> initializeAsync() {
        return CompletableFuture.runAsync(() -> {
            // Find out current language
            String langCode = this.langCodeProvider.get();
            boolean isEnglish = langCode.equalsIgnoreCase("en");
            this.logger.debug(String.format("Selected language `%s`.", langCode.toUpperCase()));

            // Retrieve suppliers from provider
            TranslationsSupplier
                externalSupplier = this.externalTSProvider.get(),
                internalSupplier = this.internalTSProvider.get(),
                fallbackSupplier = this.fallbackTSProvider.get();

            // Collect suppliers into stream
            Provider<Stream<TranslationsSupplier>> suppliersStreamProvider = () -> Stream.of(externalSupplier, internalSupplier, fallbackSupplier);
            Stream<TranslationsSupplier> suppliersStream = suppliersStreamProvider.get();

            /*
             * Filter out (don't initialize) fallback provider if the lang code is English,
             * because InternalTranslationsSupplier and FallbackTranslationsSupplier would
             * unncecessary duplicate the same ResourceBundle
             */
            if (isEnglish)
                suppliersStream = suppliersStream.filter((TranslationsSupplier supplier) -> !(supplier instanceof FallbackTranslationsSupplier));

            // Initialize suppliers
            CompletableFuture.allOf(
                suppliersStream
                    .map(TranslationsSupplier::initializeAsync)
                    .toArray(CompletableFuture<?>[]::new)
            ).join();

            // Filter out not available suppliers & collect
            this.suppliers = suppliersStreamProvider.get()
                .filter(TranslationsSupplier::isAvailable)
                .collect(Collectors.toList());


            this.suppliers.forEach((TranslationsSupplier supplier) -> this.logger.debug(
                String.format("%s (%s) %s", supplier.getClass().getSimpleName(), supplier.getLanguageCode(), supplier.isAvailable() ? "available" : "not available")
            ));
        });
    }

    public ImmutableList<TranslationsSupplier> getSuppliers() {
        return ImmutableList.copyOf(this.suppliers);
    }

    /**
     * This method lookup in all available TranslationsSupplier's for the specified key
     * and returns the translations if found, orherwise throws {@link MissingTranslationException}.
     *
     * @param key - translation key
     * @return translated text
     * @throws MissingTranslationException - in case the translation wasn't found for the specified key
     */
    public String getString(String key) throws MissingTranslationException {
        Optional<String> optionalValue = this.suppliers.stream()
            .map((TranslationsSupplier supplier) -> supplier.getString(key))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .findFirst();

        if (optionalValue.isPresent())
            return optionalValue.get();
        else
            throw new MissingTranslationException(key);
    }

}
