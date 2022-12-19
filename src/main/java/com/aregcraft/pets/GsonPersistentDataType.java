package com.aregcraft.pets;

import com.google.gson.Gson;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

public class GsonPersistentDataType<T> implements PersistentDataType<String, T> {
    private static final Gson GSON = new Gson();

    private final Class<T> complexType;

    public GsonPersistentDataType(Class<T> complexType) {
        this.complexType = complexType;
    }

    @Override
    public Class<String> getPrimitiveType() {
        return String.class;
    }

    @Override
    public Class<T> getComplexType() {
        return complexType;
    }

    @Override
    public String toPrimitive(T complex, PersistentDataAdapterContext context) {
        return GSON.toJson(complex);
    }

    @Override
    public T fromPrimitive(String primitive, PersistentDataAdapterContext context) {
        return GSON.fromJson(primitive, complexType);
    }
}
