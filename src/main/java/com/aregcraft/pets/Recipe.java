package com.aregcraft.pets;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.Map;

public class Recipe {
    private Item result;
    private String[] shape;
    private Map<Character, Material> ingredients;

    public ShapedRecipe toShapedRecipe(NamespacedKey key, ItemStack item) {
        var recipe = new ShapedRecipe(key, item);
        recipe.shape(shape);
        ingredients.forEach(recipe::setIngredient);
        return recipe;
    }

    public ItemStack getResult(ItemStack item) {
        result.withItemStack(item);
        return item;
    }
}
