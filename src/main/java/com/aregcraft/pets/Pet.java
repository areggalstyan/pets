package com.aregcraft.pets;

import com.aregcraft.delta.api.AttributeModifierBuilder;
import com.aregcraft.delta.api.FormattingContext;
import com.aregcraft.delta.api.item.ItemWrapper;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.mariuszgromada.math.mxparser.Expression;

import java.text.DecimalFormat;
import java.util.Objects;

public class Pet {
    private final PetType type;
    private double level;
    private ExperienceBooster experienceBooster;
    private int candies;

    public Pet(PetType type) {
        this.type = type;
    }

    public static Pet of(ItemWrapper item, Pets plugin) {
        return item.getPersistentData(plugin).get("pet", Pet.class);
    }

    public boolean addExperience(int experience) {
        return (int) level != (int) (level += calculate(type.level(), experience) + calculateBoost(experience));
    }

    public void setLevel(double level) {
        this.level = level;
    }

    private double calculateBoost(int experience) {
        return experienceBooster != null ? experienceBooster.calculate(experience) : 0;
    }

    public void setExperienceBooster(ExperienceBooster experienceBooster) {
        this.experienceBooster = experienceBooster;
    }

    public void addAttributeModifiers(Player player) {
        type.attributes().forEach((attribute, amount) -> AttributeModifierBuilder.forAttributable(player)
                .attribute(attribute)
                .name(type.id())
                .amount(calculateAttribute(amount))
                .add());
    }

    public void removeAttributeModifiers(Player player) {
        type.attributes().keySet().forEach(it -> removeAttributeModifier(player, it));
    }

    private void removeAttributeModifier(Player player, Attribute attribute) {
        var instance = Objects.requireNonNull(player.getAttribute(attribute));
        instance.getModifiers().stream()
                .filter(it -> it.getName().equals(type.id()))
                .forEach(instance::removeModifier);
    }

    public void applyPerk(Player player) {
        var perk = type.perk();
        if (perk != null) {
            perk.apply(player);
        }
    }

    public void unapplyPerk(Player player) {
        var perk = type.perk();
        if (perk != null) {
            perk.unapply(player);
        }
    }

    public boolean canUseCandy() {
        return candies < type.maxCandies();
    }

    public boolean useCandy(Candy candy) {
        candies++;
        return (int) level != (int) (level += candy.getExperience());
    }

    private double calculate(Expression expression, double value) {
        if (expression == null) {
            return 0;
        }
        expression.setArgumentValue("x", value);
        return expression.calculate();
    }

    private double calculateAttribute(Expression expression) {
        return calculate(expression, (int) level);
    }

    public PetType getType() {
        return type;
    }

    public String getName(Player player) {
        return FormattingContext.builder()
                .placeholder("level", (int) level)
                .placeholder("player", player.getDisplayName())
                .build().format(type.name());
    }

    public ItemWrapper getHead() {
        return type.item();
    }

    public ItemWrapper getItem(Pets plugin) {
        return ItemWrapper.wrap(getHead()).createBuilder()
                .formattingContext(getFormattingContext())
                .filterDisplayableLore()
                .persistentData(plugin, "pet", this)
                .build();
    }

    private FormattingContext getFormattingContext() {
        var builder = FormattingContext.builder()
                .placeholder("level", (int) level)
                .placeholder("maxCandies", type.maxCandies())
                .placeholder("candies", candies)
                .formatter(Double.class, new DecimalFormat()::format);
        var perk = type.perk();
        if (perk != null) {
            builder.placeholder("perk", perk.getName());
            builder.placeholder("perkDescription", perk.getDescription());
        }
        if (experienceBooster != null) {
            builder.placeholder("experienceBooster", experienceBooster.getName());
        }
        type.attributes().forEach((attribute, amount) ->
                builder.placeholder(attribute.name().toLowerCase(), calculateAttribute(amount)));
        return builder.build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        var pet = (Pet) o;
        return Double.compare(pet.level, level) == 0 && Objects.equals(type, pet.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, level);
    }
}
