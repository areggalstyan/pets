package com.aregcraft.pets;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.Optional;

public class Pet implements Identified {
    private static final String PET_KEY = "pet";

    private String id;
    private String name;
    private Head head;
    private double maxHealth;
    private double knockbackResistance;
    private double movementSpeed;
    private double attackDamage;
    private double armor;
    private double armorToughness;
    private double attackSpeed;

    public static Pet getInstance(PetItem item, Pets plugin) {
        if (item == null) {
            return null;
        }
        return plugin.getPets().get(item.getId());
    }

    public void addAttributeModifiers(Player player) {
        addAttributeModifier(player, Attribute.GENERIC_MAX_HEALTH, maxHealth);
        addAttributeModifier(player, Attribute.GENERIC_KNOCKBACK_RESISTANCE, knockbackResistance);
        addAttributeModifier(player, Attribute.GENERIC_MOVEMENT_SPEED, movementSpeed);
        addAttributeModifier(player, Attribute.GENERIC_ATTACK_DAMAGE, attackDamage);
        addAttributeModifier(player, Attribute.GENERIC_ARMOR, armor);
        addAttributeModifier(player, Attribute.GENERIC_ARMOR_TOUGHNESS, armorToughness);
        addAttributeModifier(player, Attribute.GENERIC_ATTACK_SPEED, attackSpeed);
    }

    private void addAttributeModifier(Player player, Attribute attribute, double amount) {
        getAttributeInstance(player, attribute).addModifier(createAttributeModifier(amount));
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

    private void removeAttributeModifier(Player player, Attribute attribute) {
        getAttributeModifier(player, attribute).ifPresent(getAttributeInstance(player, attribute)::removeModifier);
    }

    private AttributeModifier createAttributeModifier(double amount) {
        return new AttributeModifier(PET_KEY, amount, AttributeModifier.Operation.ADD_NUMBER);
    }

    private Optional<AttributeModifier> getAttributeModifier(Player player, Attribute attribute) {
        return getAttributeInstance(player, attribute).getModifiers().stream()
                .filter(it -> PET_KEY.equals(it.getName()))
                .findAny();
    }

    private AttributeInstance getAttributeInstance(Player player, Attribute attribute) {
        return Objects.requireNonNull(player.getAttribute(attribute));
    }

    public String getName() {
        return name;
    }

    public ItemStack getHead() {
        return head.toItemStack();
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var pet = (Pet) o;
        return id.equals(pet.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
