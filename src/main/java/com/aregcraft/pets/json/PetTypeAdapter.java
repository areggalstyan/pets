package com.aregcraft.pets.json;

import com.aregcraft.delta.api.InjectPlugin;
import com.aregcraft.delta.api.Recipe;
import com.aregcraft.delta.api.item.ItemWrapper;
import com.aregcraft.delta.api.json.JsonReader;
import com.aregcraft.delta.api.json.annotation.JsonAdapterFor;
import com.aregcraft.pets.PetType;
import com.aregcraft.pets.Pets;
import com.aregcraft.pets.Rarity;
import com.aregcraft.pets.perk.Perk;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.objecthunter.exp4j.Expression;
import org.bukkit.attribute.Attribute;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@JsonAdapterFor(PetType.class)
public class PetTypeAdapter implements JsonDeserializer<PetType>, JsonSerializer<PetType> {
    private static final TypeToken<Map<Rarity, Map<Attribute, Expression>>> ATTRIBUTES_TYPE = new TypeToken<>() {};
    private static final TypeToken<Map<Rarity, List<Perk>>> PERKS_TYPE = new TypeToken<>() {};

    @InjectPlugin
    private Pets plugin;

    @Override
    public PetType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        if (json.isJsonPrimitive()) {
            return plugin.getPets().findAny(json.getAsString());
        }
        var reader = new JsonReader(context, json);
        return new PetType(reader.getString("id"),
                reader.getString("name"),
                reader.getString("head"),
                reader.get("item", ItemWrapper.class),
                reader.get("recipe", Recipe.class),
                reader.get("level", Expression.class),
                reader.get("attributes", ATTRIBUTES_TYPE),
                reader.get("perks", PERKS_TYPE),
                reader.getInt("maxCandies"));
    }

    @Override
    public JsonElement serialize(PetType src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.id());
    }
}
