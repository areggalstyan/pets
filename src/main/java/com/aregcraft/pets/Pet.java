package com.aregcraft.pets;

import com.aregcraft.delta.api.FormattingContext;
import com.aregcraft.delta.api.ItemWrapper;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.mariuszgromada.math.mxparser.Expression;

import java.util.Objects;

public class Pet {
    private final PetType type;
    private double level;

    public Pet(PetType type) {
        this.type = type;
        addExperience(0);
    }

    public static Pet of(ItemWrapper item, Pets plugin) {
        return item.getPersistentData(plugin).get("pet", Pet.class);
    }

    public boolean addExperience(int experience) {
        return level < (level += calculate(type.level(), experience));
    }

    public void addAttributeModifiers(Player player) {
        addAttributeModifier(player, Attribute.GENERIC_MAX_HEALTH, type.maxHealth());
        addAttributeModifier(player, Attribute.GENERIC_KNOCKBACK_RESISTANCE, type.knockbackResistance());
        addAttributeModifier(player, Attribute.GENERIC_MOVEMENT_SPEED, type.movementSpeed());
        addAttributeModifier(player, Attribute.GENERIC_ATTACK_DAMAGE, type.attackDamage());
        addAttributeModifier(player, Attribute.GENERIC_ARMOR, type.armor());
        addAttributeModifier(player, Attribute.GENERIC_ARMOR_TOUGHNESS, type.armorToughness());
        addAttributeModifier(player, Attribute.GENERIC_ATTACK_SPEED, type.attackSpeed());
    }

    private void addAttributeModifier(Player player, Attribute attribute, Expression expression) {
        Objects.requireNonNull(player.getAttribute(attribute))
                .addModifier(new AttributeModifier(type.id(), calculateAttribute(expression),
                        AttributeModifier.Operation.ADD_NUMBER));
    }

    public void removeAttributeModifiers(Player player) {
        removeAttributeModifier(player, Attribute.GENERIC_MAX_HEALTH);
        removeAttributeModifier(player, Attribute.GENERIC_KNOCKBACK_RESISTANCE);
        removeAttributeModifier(player, Attribute.GENERIC_MOVEMENT_SPEED);
        removeAttributeModifier(player, Attribute.GENERIC_ATTACK_DAMAGE);
        removeAttributeModifier(player, Attribute.GENERIC_ARMOR);
        removeAttributeModifier(player, Attribute.GENERIC_ARMOR_TOUGHNESS);
        removeAttributeModifier(player, Attribute.GENERIC_ATTACK_SPEED);
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

    private void removeAttributeModifier(Player player, Attribute attribute) {
        var attributeInstance = Objects.requireNonNull(player.getAttribute(attribute));
        attributeInstance.getModifiers().stream()
                .filter(it -> it.getName().equals(type.id()))
                .forEach(attributeInstance::removeModifier);
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
        var formattingContext = FormattingContext.builder()
                .placeholder("level", (int) level)
                .placeholder("maxHealth", calculateAttribute(type.maxHealth()))
                .placeholder("knockbackResistance", calculateAttribute(type.knockbackResistance()))
                .placeholder("movementSpeed", calculateAttribute(type.movementSpeed()))
                .placeholder("attackDamage", calculateAttribute(type.attackDamage()))
                .placeholder("armor", calculateAttribute(type.armor()))
                .placeholder("armorToughness", calculateAttribute(type.armorToughness()))
                .placeholder("attackSpeed", calculateAttribute(type.attackSpeed()))
                .build();
        return getHead().copy().createBuilder()
                .nameFormattingContext(formattingContext)
                .loreFormattingContext(formattingContext)
                .persistentData(plugin, "pet", this)
                .build();
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
