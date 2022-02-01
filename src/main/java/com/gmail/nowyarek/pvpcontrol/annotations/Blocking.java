package com.gmail.nowyarek.pvpcontrol.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.CLASS;

@Target({ METHOD })
@Retention(CLASS)
public @interface Blocking {}
