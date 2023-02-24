package com.aregcraft.pets;

import com.aregcraft.delta.api.Recipe;
import com.aregcraft.delta.api.item.ItemWrapper;
import com.aregcraft.delta.api.registry.Identifiable;
import com.aregcraft.delta.api.registry.Registrable;

public class Upgrade implements Identifiable<String>, Registrable<Pets> {
    private final String id;
    private final PetType pet;
    private final Rarity rarity;
    private final ItemWrapper item;
    private final Recipe recipe;

    public Upgrade(String id, PetType pet, Rarity rarity, ItemWrapper item, Recipe recipe) {
        this.id = id;
        this.pet = pet;
        this.rarity = rarity;
        this.item = item;
        this.recipe = recipe;
    }

    public static Upgrade of(ItemWrapper item, Pets plugin) {
        return plugin.getUpgrades().findAny(item.getPersistentData(plugin).get("id", String.class));
    }

    @Override
    public String getId() {
        return id;
    }

    public PetType getPet() {
        return pet;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public ItemWrapper getItem() {
        return item;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    @Override
    public void register(Pets plugin) {
        item.getPersistentData(plugin).set("id", id);
        if (recipe != null) {
            recipe.add(plugin, id, item);
        }
    }
}
