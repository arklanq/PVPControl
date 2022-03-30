package com.gmail.nowyarek.pvpcontrol.utils;

import javax.annotation.Nullable;
import javax.inject.Provider;

public class Property<T> implements Provider<T> {
    @Nullable
    private T value = null;

    public Property() {}

    public Property(T initialValue) {
        this.value = initialValue;
    }

    public T get() {
        return value;
    }

    public void set(T newValue) {
        this.value = newValue;
    }

    public boolean isPresent() {
        return this.value != null;
    }
}
