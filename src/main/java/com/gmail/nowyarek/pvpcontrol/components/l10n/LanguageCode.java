package com.gmail.nowyarek.pvpcontrol.components.l10n;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier
@Target({ PARAMETER, METHOD, FIELD })
@Retention(RUNTIME)
public @interface LanguageCode { }
