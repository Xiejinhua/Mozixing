package com.gz.mozixing.network.gsonutil;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * @author Alex
 * @since 19/1/30
 */
public class NullIntgerToEmptyAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<T> rawType = (Class<T>) type.getRawType();
        if (rawType != Integer.class) {
            return null;
        }
        return (TypeAdapter<T>) new IntegerNullAdapter();
    }


}