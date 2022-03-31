package com.gmail.nowyarek.pvpcontrol.components.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class ResourceBundles {

    public static CompletableFuture<Optional<ResourceBundle>> fromFileSystem(File bundleFile) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                FileInputStream fileInputStream = new FileInputStream(bundleFile);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
                ResourceBundle resourceBundle = new PropertyResourceBundle(inputStreamReader);
                return Optional.of(resourceBundle);
            } catch (FileNotFoundException e) {
                return Optional.empty();
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        });
    }

    public static CompletableFuture<Optional<ResourceBundle>> fromJar(String bundlePackage, Locale locale) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                ResourceBundle resourceBundle = ResourceBundle.getBundle(bundlePackage, locale, new ResourceBundleUTF8Control());
                return Optional.of(resourceBundle);
            } catch (MissingResourceException e) {
                return Optional.empty();
            }
        });
    }

}
