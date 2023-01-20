package com.aregcraft.pets.json;

import com.aregcraft.delta.api.Recipe;
import com.aregcraft.delta.api.item.ItemWrapper;
import com.aregcraft.delta.api.json.JsonReader;
import com.aregcraft.delta.api.json.annotation.JsonAdapterFor;
import com.aregcraft.pets.ExperienceBooster;
import com.aregcraft.pets.Pets;
import com.google.gson.*;
import org.mariuszgromada.math.mxparser.Expression;

import java.lang.reflect.Type;

@JsonAdapterFor(ExperienceBooster.class)
public class ExperienceBoosterAdapter implements JsonDeserializer<ExperienceBooster>,
        JsonSerializer<ExperienceBooster> {
    private Pets plugin;

    @Override
    public ExperienceBooster deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        if (json.isJsonPrimitive()) {
            return plugin.getExperienceBooster(json.getAsString());
        }
        var reader = new JsonReader(context, json);
        return new ExperienceBooster(reader.getString("id"),
                reader.get("item", ItemWrapper.class),
                reader.get("recipe", Recipe.class),
                reader.get("boost", Expression.class));
    }

    @Override
    public JsonElement serialize(ExperienceBooster src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src.getId());
    }
}
