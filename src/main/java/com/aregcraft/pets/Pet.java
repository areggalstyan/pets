package com.aregcraft.pets;

import com.aregcraft.delta.api.AttributeModifierBuilder;
import com.aregcraft.delta.api.FormattingContext;
import com.aregcraft.delta.api.item.ItemWrapper;
import com.aregcraft.pets.perk.Perk;
import net.objecthunter.exp4j.Expression;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.*;

public class Pet {
    private final PetType type;
    private double level;
    private ExperienceBooster experienceBooster;
    private int candies;
    private Rarity rarity;

    public Pet(PetType type, Pets plugin) {
        this(type, plugin.getDefaultRarity());
    }

    public Pet(PetType type, Rarity rarity) {
        this.type = type;
        this.rarity = rarity;
    }

    public static Pet of(ItemWrapper item, Pets plugin) {
        return item.getPersistentData(plugin).get("pet", Pet.class);
    }

    public boolean addExperience(int experience) {
        var maxLevel = type.maxLevel();
        if (maxLevel > 0 && level >= maxLevel) {
            return false;
        }
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
        getAttributes().forEach((attribute, amount) -> AttributeModifierBuilder.forAttributable(player)
                .attribute(attribute)
                .name(type.id())
                .amount(calculateAttribute(amount))
                .add());
    }

    public void removeAttributeModifiers(Player player) {
        getAttributes().keySet().forEach(it -> removeAttributeModifier(player, it));
    }

    private void removeAttributeModifier(Player player, Attribute attribute) {
        var instance = Objects.requireNonNull(player.getAttribute(attribute));
        instance.getModifiers().stream()
                .filter(it -> it.getName().equals(type.id()))
                .forEach(instance::removeModifier);
    }

    public void applyPerks(Player player) {
        var perks = getPerks();
        if (perks != null) {
            perks.forEach(it -> it.apply(player));
        }
    }

    public void unapplyPerks(Player player) {
        var perks = getPerks();
        if (perks != null) {
            perks.forEach(it -> it.unapply(player));
        }
    }

    private List<Perk> getPerks() {
        return Optional.ofNullable(type.perks()).map(it -> it.get(rarity)).orElse(null);
    }

    public boolean canUseCandy() {
        return candies < type.maxCandies();
    }

    public boolean useCandy(Candy candy) {
        candies++;
        return (int) level != (int) (level += candy.getExperience());
    }

    public boolean canUpgrade(Upgrade upgrade) {
        return upgrade.getPet().equals(type) && upgrade.getRarity().compareLevel(rarity) == 1;
    }

    public void applyUpgrade(Upgrade upgrade, Player player) {
        removeAttributeModifiers(player);
        unapplyPerks(player);
        rarity = upgrade.getRarity();
        addAttributeModifiers(player);
        applyPerks(player);
    }

    private double calculate(Expression expression, double value) {
        if (expression == null) {
            return 0;
        }
        expression.setVariable("x", value);
        return expression.evaluate();
    }

    private double calculateAttribute(Expression expression) {
        return calculate(expression, (int) level);
    }

    public PetType getType() {
        return type;
    }

    public String getName(Player player, Pets plugin) {
        return FormattingContext.builder()
                .plugin(plugin)
                .placeholder("level", (int) level)
                .placeholder("player", player.getDisplayName())
                .build().format(type.name());
    }

    public ItemWrapper getHead() {
        return type.item();
    }

    public ItemWrapper getItem(Pets plugin) {
        return ItemWrapper.wrap(getHead()).createBuilder()
                .formattingContext(getFormattingContext(plugin))
                .filterDisplayableLore()
                .persistentData(plugin, "pet", this)
                .build();
    }

    public Rarity getRarity() {
        return rarity;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    private FormattingContext getFormattingContext(Pets plugin) {
        var builder = FormattingContext.builder()
                .plugin(plugin)
                .placeholder("level", (int) level)
                .placeholder("maxCandies", type.maxCandies())
                .placeholder("candies", candies)
                .placeholder("rarity", rarity.getName())
                .formatter(Double.class, new DecimalFormat()::format);
        var perks = getPerks();
        if (perks != null) {
            var value = new ArrayList<String>();
            perks.forEach(it -> {
                value.add(it.getName());
                value.addAll(it.getDescription());
                value.add("");
            });
            value.remove(value.size() - 1);
            builder.placeholder("perks", value);
        }
        if (experienceBooster != null) {
            builder.placeholder("experienceBooster", experienceBooster.getName());
        }
        getAttributes().forEach((attribute, amount) ->
                builder.placeholder(attribute.name().toLowerCase(), calculateAttribute(amount)));
        return builder.build();
    }

    private Map<Attribute, Expression> getAttributes() {
        return type.attributes().get(rarity);
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

    @Override
    public String toString() {
        return "Pet{" +
                "type=" + type +
                ", level=" + level +
                ", experienceBooster=" + experienceBooster +
                ", candies=" + candies +
                '}';
    }
}
