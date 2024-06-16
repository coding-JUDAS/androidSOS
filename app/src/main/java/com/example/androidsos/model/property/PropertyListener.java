package com.example.androidsos.model.property;

@FunctionalInterface
public interface PropertyListener<T>{
    void valueChanged(Property<T> property, T oldValue, T newValue);
}
