package com.aregcraft.pets;

import com.aregcraft.delta.api.registry.Identifiable;

public class Rarity implements Identifiable<String> {
    private final String id;
    private final String name;
    private final int level;

    public Rarity(String id, String name, int level) {
        this.id = id;
        this.name = name;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getId() {
        return id;
    }

    public int compareRarity(Rarity o) {
        return level - o.level;
    }
}
