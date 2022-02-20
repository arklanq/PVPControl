package com.gmail.nowyarek.pvpcontrol.components.resources;

import java.util.ResourceBundle;
import java.util.Set;

public class ResourceBundleCompletenessValidator {

    /**
     * @param base   - the complete {@link ResourceBundle} on which calculations are based
     * @param target - the probably incomplete {@link ResourceBundle} for which we will be calculating the results
     * @return Percentage of the {@link ResourceBundle} completness
     */
    public static double checkCompletness(ResourceBundle base, ResourceBundle target) {
        Set<String> requiredKeys = base.keySet();
        int missingKeys = 0;

        for (String key : requiredKeys) {
            if (!target.containsKey(key)) missingKeys++;
        }

        return ((double) (requiredKeys.size() - missingKeys)) / (requiredKeys.size()) * 100;
    }

}
