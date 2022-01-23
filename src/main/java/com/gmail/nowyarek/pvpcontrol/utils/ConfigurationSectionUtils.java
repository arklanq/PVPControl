package com.gmail.nowyarek.pvpcontrol.utils;

public class ConfigurationSectionUtils {

    public static String joinPath(String basePath, String path) {
        String output = String.format("%s.%s", basePath, path);

        if(output.charAt(0) == '.')
            output = output.substring(1);

        return output;
    }

}
