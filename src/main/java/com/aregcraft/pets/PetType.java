package com.aregcraft.pets;

import com.aregcraft.delta.api.ItemWrapper;
import com.aregcraft.delta.api.Recipe;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.SkullMeta;
import org.mariuszgromada.math.mxparser.Expression;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.UUID;

public record PetType(String id, String name, String head, ItemWrapper item, Recipe recipe, Expression level,
                      Expression maxHealth, Expression knockbackResistance, Expression movementSpeed,
                      Expression attackDamage, Expression armor, Expression armorToughness, Expression attackSpeed) {
    public void register(Pets plugin) {
        if (item.getMeta() instanceof SkullMeta meta) {
            var profile = Bukkit.createPlayerProfile(UUID.randomUUID());
            try {
                profile.getTextures().setSkin(new URL("https://textures.minecraft.net/texture/" + head));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            meta.setOwnerProfile(profile);
            item.setMeta(meta);
        }
        recipe.register(new NamespacedKey(plugin, id), new Pet(this).getItem(plugin));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return Objects.equals(id, ((PetType) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
