package com.gmail.nowyarek.pvpcontrol.components.plugin;

import jakarta.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier
@Target({ PARAMETER, METHOD, FIELD })
@Retention(RUNTIME)
public @interface PluginVersion { }
