package com.aregcraft.pets.json;

import com.aregcraft.delta.api.InjectPlugin;
import com.aregcraft.delta.api.json.JsonReader;
import com.aregcraft.delta.api.json.annotation.JsonAdapterFor;
import com.aregcraft.pets.Pets;
import com.aregcraft.pets.Rarity;
import com.google.gson.*;

import java.lang.reflect.Type;

@JsonAdapterFor(Rarity.class)
public class RarityAdapter implements JsonDeserializer<Rarity>, JsonSerializer<Rarity> {
    @InjectPlugin
    private Pets plugin;

    @Override
    public Rarity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        if (json.isJsonPrimitive()) {
            return plugin.getRarities().findAny(json.getAsString());
        }
        var reader = new JsonReader(context, json);
        return new Rarity(reader.getString("id"),
                reader.getString("name"),
                reader.getInt("level"));
    }

    @Override
    public JsonElement serialize(Rarity src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src.getId());
    }
}
