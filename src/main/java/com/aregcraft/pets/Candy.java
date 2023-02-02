package com.aregcraft.pets;

import com.aregcraft.delta.api.Identifiable;
import com.aregcraft.delta.api.Recipe;
import com.aregcraft.delta.api.item.ItemWrapper;

public class Candy implements Identifiable<String> {
    private final String id;
    private final ItemWrapper item;
    private final Recipe recipe;
    private final double experience;

    public Candy(String id, ItemWrapper item, Recipe recipe, double experience) {
        this.id = id;
        this.item = item;
        this.recipe = recipe;
        this.experience = experience;
    }

    public static Candy of(ItemWrapper item, Pets plugin) {
        return plugin.getCandy(item.getPersistentData(plugin).get("id", String.class));
    }

    public double getExperience() {
        return experience;
    }

    public void register(Pets plugin) {
        item.getPersistentData(plugin).set("id", id);
        if (recipe != null) {
            recipe.add(plugin, id, item);
        }
    }

    @Override
    public String getId() {
        return id;
    }

    public ItemWrapper getItem() {
        return item;
    }

    public Recipe getRecipe() {
        return recipe;
    }
}
