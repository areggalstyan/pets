package com.aregcraft.pets.perk;

import com.aregcraft.delta.api.json.JsonReader;
import com.aregcraft.delta.api.json.annotation.JsonAdapterFor;
import com.aregcraft.delta.api.util.Classes;
import com.aregcraft.pets.Pets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import org.bukkit.event.Listener;

import java.lang.reflect.Type;

@JsonAdapterFor(Perk.class)
public class PerkDeserializer implements JsonDeserializer<Perk> {
    private static final String CLASS_NAME_TEMPLATE = "com.aregcraft.pets.perk.%sPerk";

    private Pets plugin;

    @Override
    public Perk deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        if (json.isJsonPrimitive()) {
            return plugin.getPerk(json.getAsString());
        }
        var reader = new JsonReader(context, json);
        var perk = reader.deserialize(json, getClass(reader.getString("base")));
        Classes.setField(Perk.class, perk, "plugin", plugin);
        if (perk instanceof Listener listener) {
            plugin.registerListener(listener);
        }
        return perk;
    }

    private Class<? extends Perk> getClass(String base) {
        return Classes.getClass(CLASS_NAME_TEMPLATE.formatted(base));
    }
}
