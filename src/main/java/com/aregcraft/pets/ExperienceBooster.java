package com.aregcraft.pets;

import com.aregcraft.delta.api.Recipe;
import com.aregcraft.delta.api.item.ItemWrapper;
import com.aregcraft.delta.api.registry.Identifiable;
import com.aregcraft.delta.api.registry.Registrable;
import net.objecthunter.exp4j.Expression;

public class ExperienceBooster implements Identifiable<String>, Registrable<Pets> {
    private final String id;
    private final ItemWrapper item;
    private final Recipe recipe;
    private final Expression boost;

    public ExperienceBooster(String id, ItemWrapper item, Recipe recipe, Expression boost) {
        this.id = id;
        this.item = item;
        this.recipe = recipe;
        this.boost = boost;
    }

    public static ExperienceBooster of(ItemWrapper item, Pets plugin) {
        return plugin.getExperienceBoosters().findAny(item.getPersistentData(plugin).get("id", String.class));
    }

    public String getName() {
        return item.getUnformattedName();
    }

    @Override
    public void register(Pets plugin) {
        item.getPersistentData(plugin).set("id", id);
        if (recipe != null) {
            recipe.add(plugin, id, item);
        }
    }

    @Override
    public void unregister(Pets plugin) {
        if (recipe != null) {
            recipe.remove(plugin, id);
        }
    }

    public double calculate(int experience) {
        boost.setVariable("x", experience);
        return boost.evaluate();
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
