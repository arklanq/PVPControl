package com.gmail.nowyarek.pvpcontrol.annotations;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier
@Target({ FIELD, PARAMETER, METHOD })
@Retention(RUNTIME)
public @interface PluginLogger { }
