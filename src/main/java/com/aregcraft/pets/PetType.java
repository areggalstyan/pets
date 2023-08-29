package com.aregcraft.pets;

import com.aregcraft.delta.api.Recipe;
import com.aregcraft.delta.api.item.ItemWrapper;
import com.aregcraft.delta.api.registry.Identifiable;
import com.aregcraft.delta.api.registry.Registrable;
import com.aregcraft.pets.perk.Perk;
import net.objecthunter.exp4j.Expression;
import org.bukkit.attribute.Attribute;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public record PetType(String id, String name, String head, ItemWrapper item, Recipe recipe, Expression level,
                      Map<Rarity, Map<Attribute, Expression>> attributes, Map<Rarity, List<Perk>> perks,
                      int maxCandies, int maxLevel) implements Identifiable<String>, Registrable<Pets> {
    @Override
    public void register(Pets plugin) {
        new HeadTexture(head).apply(item);
        if (recipe != null) {
            recipe.add(plugin, id, new Pet(this, plugin).getItem(plugin));
        }
    }

    @Override
    public void unregister(Pets plugin) {
        if (recipe != null) {
            recipe.remove(plugin, id);
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

    @Override
    public String toString() {
        return "PetType{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", head='" + head + '\'' +
                ", item=" + item +
                ", recipe=" + recipe +
                ", level=" + level +
                ", attributes=" + attributes +
                ", perks=" + perks +
                ", maxCandies=" + maxCandies +
                ", maxLevel=" + maxLevel +
                '}';
    }
}
