package com.aregcraft.pets;

import com.aregcraft.delta.api.registry.Identifiable;

public class Rarity implements Identifiable<String>, Comparable<Rarity> {
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

    @Override
    public int compareTo(Rarity o) {
        return compareLevel(o);
    }

    public int compareLevel(Rarity o) {
        return level - o.level;
    }
}
