package com.aregcraft.pets;

import com.aregcraft.delta.api.Identifiable;
import com.aregcraft.delta.api.Recipe;
import com.aregcraft.delta.api.item.ItemWrapper;
import com.aregcraft.pets.perk.Perk;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.SkullMeta;
import org.mariuszgromada.math.mxparser.Expression;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public record PetType(String id, String name, String head, ItemWrapper item, Recipe recipe, Expression level,
                      Map<Attribute, Expression> attributes, Perk perk, int maxCandies)
        implements Identifiable<String> {
    public void register(Pets plugin) {
        item.<SkullMeta>editMeta(it -> {
            var profile = Bukkit.createPlayerProfile(UUID.randomUUID());
            try {
                profile.getTextures().setSkin(new URL("https://textures.minecraft.net/texture/" + head));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            it.setOwnerProfile(profile);
        });
        if (recipe != null) {
            recipe.add(plugin, id, new Pet(this).getItem(plugin));
        }
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

    @Override
    public String getId() {
        return id;
    }
}
