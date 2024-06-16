package com.example.androidsos.model.property;

import java.util.ArrayList;
import java.util.Arrays;

public class Property<T> {
    private T value, oldValue;
    private Object owner;
    private ArrayList<PropertyListener> listeners;

    public Property(Object owner, T initialValue) {
        listeners = new ArrayList<>();
        this.owner = owner;
        this.value = initialValue;
    }

    public Object getOwner(){
        return owner;
    }
    public T get(){
        return value;
    }

    public void set(T newValue){
        oldValue = value;
        this.value = newValue;
        notifyListeners();
    }
    public void addListener(PropertyListener<T> listener){
        listeners.add(listener);
    }
    public void addListeners(PropertyListener<T>...Listeners){
        listeners.addAll(Arrays.asList(Listeners));
    }
    public void removeListener(PropertyListener<T> listener){
        listeners.remove(listener);
    }
    protected void notifyListeners(){
        for(PropertyListener<T> propertyListener: listeners){
            propertyListener.valueChanged(this, oldValue, value);
        }
    }
}
