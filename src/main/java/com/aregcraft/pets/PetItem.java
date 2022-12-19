package com.aregcraft.pets;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class PetItem implements Identified {
    private static final String PET_ID_KEY = "pet_id";

    private String id;
    private Recipe recipe;

    public static PetItem getInstance(Pet pet, Pets plugin) {
        return plugin.getPetItems().get(pet.getId());
    }

    public static PetItem getInstance(ItemStack item, Pets plugin) {
        return plugin.getPetItems().get(getPetId(item, plugin));
    }

    private static String getPetId(ItemStack item, Pets plugin) {
        return Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer()
                .get(new NamespacedKey(plugin, PET_ID_KEY), PersistentDataType.STRING);
    }

    public void registerRecipe(Pets plugin) {
        Bukkit.addRecipe(recipe.toShapedRecipe(new NamespacedKey(plugin, id), getItemStack(plugin)));
    }

    public void unregisterRecipe(Pets plugin) {
        Bukkit.removeRecipe(new NamespacedKey(plugin, id));
    }

    public String getId() {
        return id;
    }

    public ItemStack getItemStack(Pets plugin) {
        var item = Pet.getInstance(this, plugin).getHead().clone();
        var meta = Objects.requireNonNull(item.getItemMeta());
        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, PET_ID_KEY), PersistentDataType.STRING, id);
        item.setItemMeta(meta);
        return recipe.getResult(item);
    }
}
