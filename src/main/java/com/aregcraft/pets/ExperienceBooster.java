package com.aregcraft.pets;

import com.aregcraft.delta.api.Recipe;
import com.aregcraft.delta.api.item.ItemWrapper;
import org.mariuszgromada.math.mxparser.Expression;

public class ExperienceBooster {
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
        return plugin.getExperienceBooster(item.getPersistentData(plugin).get("id", String.class));
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return item.getUnformattedName();
    }

    public void register(Pets plugin) {
        item.getPersistentData(plugin).set("id", id);
        recipe.add(plugin, id, item);
    }

    public double calculate(int experience) {
        boost.setArgumentValue("x", experience);
        return boost.calculate();
    }
}
