package com.gmail.nowyarek.pvpcontrol.utils;

import com.google.common.base.Verify;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ResourcesUtils {

    public static String[] getJarResourceListing(String path, boolean recursive) throws IOException {
        String pathWithoutPrecedingSlash = path.startsWith("/") ? path.substring(1) : path;
        URL dirURL = ResourcesUtils.class.getResource(path);
        Objects.requireNonNull(
            dirURL,
            String.format("Resource for path `%s` was not found or we don't have adequate privileges to get the resource.", path)
        );
        String dirPath = dirURL.getPath();

        Verify.verify(dirURL.getProtocol().equals("jar"), "URL protocol is different than `jar`.");

        String jarPath = dirPath.substring(5, dirPath.indexOf("!")); // Strip out only the JAR file

        try (JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"))) {
            Enumeration<JarEntry> entries = jar.entries(); // Gives all entries in the jar file
            Set<String> listingSet = new HashSet<>();

            while (entries.hasMoreElements()) {
                String name = entries.nextElement().getName();

                //filter according to the path
                if (name.startsWith(pathWithoutPrecedingSlash)) {
                    String entry = name.substring(pathWithoutPrecedingSlash.length());

                    if(entry.charAt(entry.length() - 1) != '/') {
                        if(!recursive && entry.lastIndexOf("/") != 0)
                            continue;

                        listingSet.add(entry.substring(1));
                    }
                }
            }

            return listingSet.toArray(new String[0]);
        }
    }

}
